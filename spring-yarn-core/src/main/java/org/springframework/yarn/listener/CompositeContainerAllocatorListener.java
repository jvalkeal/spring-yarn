package org.springframework.yarn.listener;

import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.springframework.yarn.am.allocate.ContainerAllocatorListener;

public class CompositeContainerAllocatorListener implements ContainerAllocatorListener {

    private OrderedComposite<ContainerAllocatorListener> listeners = new OrderedComposite<ContainerAllocatorListener>();

    public void setListeners(List<? extends ContainerAllocatorListener> listeners) {
        this.listeners.setItems(listeners);
    }

    public void register(ContainerAllocatorListener listener) {
        listeners.add(listener);
    }

    @Override
    public void allocated(List<Container> allocatedContainers) {
        for (Iterator<ContainerAllocatorListener> iterator = listeners.reverse(); iterator.hasNext();) {
            ContainerAllocatorListener listener = iterator.next();
            listener.allocated(allocatedContainers);
        }
    }

    @Override
    public void completed(List<ContainerStatus> completedContainers) {
        for (Iterator<ContainerAllocatorListener> iterator = listeners.reverse(); iterator.hasNext();) {
            ContainerAllocatorListener listener = iterator.next();
            listener.completed(completedContainers);
        }
    }

}
