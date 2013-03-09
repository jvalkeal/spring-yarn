package org.springframework.yarn.batch.am;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.yarn.am.AbstractProcessingAppmaster;
import org.springframework.yarn.am.AppmasterService;
import org.springframework.yarn.am.YarnAppmaster;

/**
 * Implementation of application master which can be used to
 * run Spring Batch jobs on Hadoop Yarn cluster.
 * <p>
 * Application master will act as a context running the Spring
 * Batch job. Order to make some sense in terms of using cluster
 * resources, job itself should be able to partition itself in
 * a way that Yarn containers can be used to split the load.
 * <p>
 * Application master itself acts as a {@link PartitionHandler}
 * and a {@link Partitioner}.
 * 
 * @author Janne Valkealahti
 *
 */
public class BatchAppmaster extends AbstractProcessingAppmaster implements YarnAppmaster, Partitioner {

    private static final Log log = LogFactory.getLog(BatchAppmaster.class);
    
    private JobLauncher jobLauncher;
    
    private String jobName;
    
//    private int gridSize = 1;
    
//    private String stepName = "remoteStep";
    
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void submitApplication() {
        start();        
        try {
            Job job = (Job) applicationContext.getBean(jobName);
            log.debug("launching job:" + job);
            jobLauncher.run(job, new JobParameters());
        } catch (Exception e) {
            log.error("Error running job", e);
        }
    }

    @Override
    public void waitForCompletion() {
        for (int i = 0; i < 30; i++) {
            try {
                if (getMonitor().isCompleted()) {
                    log.debug("got complete from monitor");
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                log.info("sleep error", e);
            }
        }
        log.debug("waiting latch done, doing stop");
        stop();
    }
    
    @Override
    protected void doStart() {
        super.doStart();
        
        AppmasterService service = getAppmasterService();
        log.info("AppmasterService: " + service);
        if(service != null && service.hasPort()) {
            for(int i=0; i<10; i++) {
                if(service.getPort() == -1) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                } else {
                    break;
                }
                // should fail
            }
        }
        
        if(getAllocator() instanceof SmartLifecycle) {
            ((SmartLifecycle)getAllocator()).start();
        }
        
        
        if(getAppmasterService() instanceof SmartLifecycle) {
            ((SmartLifecycle)getAppmasterService()).start();
        }
        
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> map = new HashMap<String, ExecutionContext>(gridSize);
        for (int i = 0; i < gridSize; i++) {
            map.put("partition" + i, new ExecutionContext());
        }
        return map;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public ContainerLaunchContext preLaunch(ContainerLaunchContext context) {
        AppmasterService service = getAppmasterService();
        log.debug("intercept launch: " + context);
        if(service != null) {
            int port = service.getPort();
            String address = service.getHost();
            log.debug("intercept launch: port is" + port);
            Map<String, String> env = new HashMap(context.getEnvironment());
            env.put("amservice.port", Integer.toString(port));
            env.put("amservice.address", address);
            context.setEnvironment(env);
            return context;
        } else {
            return super.preLaunch(context);            
        }
    }

    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    
    /**
     * Passed to the {@link StepExecutionSplitter} in the
     * {@link #handle(StepExecutionSplitter, StepExecution)} method, instructing
     * it how many {@link StepExecution} instances are required, ideally. The
     * {@link StepExecutionSplitter} is allowed to ignore the grid size in the
     * case of a restart, since the input data partitions must be preserved.
     * 
     * @param gridSize the number of step executions that will be created
     */
//    public void setGridSize(int gridSize) {
//        this.gridSize = gridSize;
//    }

    /**
     * The name of the {@link Step} that will be used to execute the partitioned
     * {@link StepExecution}. This is a regular Spring Batch step, with all the
     * business logic required to complete an execution based on the input
     * parameters in its {@link StepExecution} context. The name will be
     * translated into a {@link Step} instance by the remote worker.
     * 
     * @param stepName the name of the {@link Step} instance to execute business logic
     */
//    public void setStepName(String stepName) {
//        this.stepName = stepName;
//    }

}
