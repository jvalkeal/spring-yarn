package org.springframework.yarn.am;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.springframework.yarn.am.allocate.ContainerAllocator;
import org.springframework.yarn.am.allocate.ContainerAllocatorListener;
import org.springframework.yarn.am.allocate.DefaultContainerAllocator;
import org.springframework.yarn.am.container.ContainerLauncher;
import org.springframework.yarn.am.container.DefaultContainerLauncher;
import org.springframework.yarn.am.monitor.ContainerMonitor;
import org.springframework.yarn.am.monitor.DefaultContainerMonitor;

/**
 * Base application master implementation which handles a simple
 * life-cycle scenario of; allocate, launch, monitor.
 * <p>
 * We can say that the actual implementation of this is very static
 * in terms of what application master can do. Everything needs
 * to be known prior to starting the life-cycle. Implementation
 * should know how many containers will participate the application,
 * what those containers will do and what is the expected outcome
 * from a container execution.
 * 
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractProcessingAppmaster extends AbstractAppmaster implements ContainerLauncherInterceptor {

    private static final Log log = LogFactory.getLog(AbstractProcessingAppmaster.class);

    private ContainerAllocator allocator;
    private ContainerLauncher launcher;
    private ContainerMonitor monitor;
        
    @Override
    protected void onInit() throws Exception {
        super.onInit();
        
        if(allocator == null) {
            allocator = new DefaultContainerAllocator(getTemplate(), getApplicationAttemptId());
        }
        
        if(launcher == null) {
            launcher = new DefaultContainerLauncher(getConfiguration(), getResourceLocalizer(), getEnvironment());
            ((DefaultContainerLauncher)launcher).setContainerLauncherInterceptor(this);
        }
        
        if(monitor == null) {
            monitor = new DefaultContainerMonitor();
        }
        
        if(log.isDebugEnabled()) {
            log.debug("Using handlers allocator=" + allocator +
                    " launcher=" + launcher +
                    " monitor=" + monitor);
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

    @Override
    public ContainerLaunchContext preLaunch(ContainerLaunchContext context) {
        return context;
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
