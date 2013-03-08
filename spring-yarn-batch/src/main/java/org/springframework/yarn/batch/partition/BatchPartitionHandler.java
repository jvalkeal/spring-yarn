package org.springframework.yarn.batch.partition;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.StepExecutionSplitter;
import org.springframework.yarn.am.AbstractProcessingAppmaster;

/**
 * 
 * 
 * @author Janne Valkealahti
 *
 */
public class BatchPartitionHandler implements PartitionHandler {

    private static final Log log = LogFactory.getLog(BatchPartitionHandler.class);
    
//    private String stepName = "remoteStep";
    private int gridSize = 1;
    private AbstractProcessingAppmaster master;
    
    public BatchPartitionHandler(AbstractProcessingAppmaster master) {
        this.master = master;
    }

    @Override
    public Collection<StepExecution> handle(StepExecutionSplitter stepSplitter, StepExecution stepExecution)
            throws Exception {
        
        Set<StepExecution> split = stepSplitter.split(stepExecution, gridSize);
        master.getMonitor().setTotal(gridSize);
        master.getAllocator().allocateContainers(gridSize);
        
        // monitor
        for(int i = 0; i<30; i++) {
            try {
                if(master.getMonitor().isCompleted()) {
                    log.debug("got complete from monitor");
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                log.info("sleep error", e);
            }
        }
        log.debug("setting completed:");
        master.getMonitor().setCompleted();
        
        //Collection<StepExecution>        
        return split;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

}
