package org.springframework.yarn.am;

import java.util.List;

import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.springframework.yarn.am.allocate.ContainerAllocator;
import org.springframework.yarn.am.allocate.ContainerAllocatorListener;
import org.springframework.yarn.am.allocate.DefaultContainerAllocator;
import org.springframework.yarn.am.container.ContainerLauncher;
import org.springframework.yarn.am.container.DefaultContainerLauncher;
import org.springframework.yarn.am.monitor.ContainerMonitor;
import org.springframework.yarn.am.monitor.DefaultContainerMonitor;

/**
 * Base application master implementation which handles a very simple
 * use case of its lifycycle. 
 * 
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractProcessingAppmaster extends AbstractAppmaster {

    //private static final Log log = LogFactory.getLog(AbstractProcessingAppmaster.class);

    private ContainerAllocator allocator;
    private ContainerLauncher launcher;
    private ContainerMonitor monitor;
        
    public AbstractProcessingAppmaster() {
    }
    
    @Override
    protected void onInit() throws Exception {
        super.onInit();
        
        if(allocator == null) {
            allocator = new DefaultContainerAllocator(getTemplate(), getApplicationAttemptId());
        }
        
        if(launcher == null) {
            launcher = new DefaultContainerLauncher(getConfiguration(), getResourceLocalizer(), getEnvironment());
        }
        
        if(monitor == null) {
            monitor = new DefaultContainerMonitor();
        }
        
        allocator.addListener(new ContainerAllocatorListener() {            
            @Override
            public void allocated(List<Container> allocatedContainers) {
                for(Container container : allocatedContainers) {
                    launcher.launchContainer(container, getCommands());                
                }
            }
            @Override
            public void completed(List<ContainerStatus> completedContainers) {
                monitor.monitorContainer(completedContainers);
            }            
        });        
        
    }

    public ContainerAllocator getAllocator() {
        return allocator;
    }

    public void setAllocator(ContainerAllocator allocator) {
        this.allocator = allocator;
    }

    public ContainerLauncher getLauncher() {
        return launcher;
    }

    public void setLauncher(ContainerLauncher launcher) {
        this.launcher = launcher;
    }

    public ContainerMonitor getMonitor() {
        return monitor;
    }

    public void setMonitor(ContainerMonitor monitor) {
        this.monitor = monitor;
    }

}
