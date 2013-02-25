package org.springframework.yarn.am.allocate;


public interface ContainerAllocator {
    
    void allocateContainers(int count);
    void addListener(ContainerAllocatorListener listener);

}
