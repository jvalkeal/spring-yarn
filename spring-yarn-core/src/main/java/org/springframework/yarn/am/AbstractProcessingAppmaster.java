package org.springframework.yarn.am;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.springframework.util.Assert;
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

    /** Container allocator for this class */
    private ContainerAllocator allocator;
    
    /** Container launcher for this class */
    private ContainerLauncher launcher;
    
    /** Container monitor for this class */
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

    /**
     * Gets a used {@link ContainerAllocator} for this class.
     * 
     * @return {@link ContainerAllocator} used in this class
     */
    public ContainerAllocator getAllocator() {
        return allocator;
    }

    /**
     * Sets the {@link ContainerAllocator} used for this class.
     * This should be called before {@link #onInit() onInit} for this class
     * is called.
     * 
     * @param allocator the {@link ContainerAllocator}
     */
    public void setAllocator(ContainerAllocator allocator) {
        Assert.isNull(this.allocator, "ContainerAllocator is already set");
        this.allocator = allocator;
    }

    /**
     * Gets a used {@link ContainerLauncher} for this class.
     * 
     * @return {@link ContainerLauncher} used in this class
     */
    public ContainerLauncher getLauncher() {
        return launcher;
    }

    /**
     * Sets the {@link ContainerLauncher} used for this class.
     * This should be called before {@link #onInit() onInit} for this class
     * is called.
     * 
     * @param launcher the {@link ContainerLauncher}
     */
    public void setLauncher(ContainerLauncher launcher) {
        Assert.isNull(this.launcher, "ContainerLauncher is already set");
        this.launcher = launcher;
    }

    /**
     * Gets a used {@link ContainerMonitor} for this class.
     * 
     * @return {@link ContainerMonitor} used in this class
     */
    public ContainerMonitor getMonitor() {
        return monitor;
    }

    /**
     * Sets the {@link ContainerMonitor} used for this class.
     * This should be called before {@link #onInit() onInit} for this class
     * is called.
     * 
     * @param monitor the {@link ContainerMonitor}
     */
    public void setMonitor(ContainerMonitor monitor) {
        Assert.isNull(this.monitor, "ContainerMonitor is already set");
        this.monitor = monitor;
    }

}
