package org.springframework.yarn.am.monitor;

import java.util.List;

import org.apache.hadoop.yarn.api.records.ContainerStatus;

public interface ContainerMonitor {

    void setTotal(int count);
    boolean isCompleted();
    void monitorContainer(List<ContainerStatus> completedContainers);
    void setCompleted();
    
}
