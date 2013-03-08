package org.springframework.yarn.am.monitor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.ContainerStatus;

public class DefaultContainerMonitor implements ContainerMonitor {

    private static final Log log = LogFactory.getLog(DefaultContainerMonitor.class);
    
    private int total = -1;
    private int completed = 0;
    
    public DefaultContainerMonitor() {
    }

    @Override
    public void monitorContainer(List<ContainerStatus> completedContainers) {
        completed += completedContainers.size();
    }

    @Override
    public void setTotal(int count) {
        total = count;
    }
    
    @Override
    public boolean isCompleted() {
        log.debug("complete: completed=" + completed + " total=" + total);
        return completed >= total;
    }

    @Override
    public void setCompleted() {
        completed = total;
    }

}
