package org.springframework.yarn.am.allocate;

import java.util.List;

import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerStatus;

public interface ContainerAllocatorListener {
    
    void allocated(List<Container> allocatedContainers);
    void completed(List<ContainerStatus> completedContainers);

}
