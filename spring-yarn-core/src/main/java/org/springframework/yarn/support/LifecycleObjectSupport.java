package org.springframework.yarn.support;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;

/**
 * Convenient base class for object which needs spring task scheduler, task
 * executor and life cycle handling.
 * 
 * @author Janne Valkealahti
 * 
 */
public abstract class LifecycleObjectSupport implements InitializingBean, SmartLifecycle {

    private static final Log log = LogFactory.getLog(LifecycleObjectSupport.class);

    // fields for lifecycle
    private volatile boolean autoStartup = true;
    private volatile int phase = 0;
    private volatile boolean running;

    // lock to protect lifycycle methods
    private final ReentrantLock lifecycleLock = new ReentrantLock();

    // common task handling
    private TaskScheduler taskScheduler;
    private TaskExecutor taskExecutor;

    @Override
    public final void afterPropertiesSet() {
        try {
            this.onInit();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new BeanInitializationException("failed to initialize", e);
        }
    }

    @Override
    public final boolean isAutoStartup() {
        return this.autoStartup;
    }

    @Override
    public final int getPhase() {
        return this.phase;
    }

    @Override
    public final boolean isRunning() {
        this.lifecycleLock.lock();
        try {
            return this.running;
        } finally {
            this.lifecycleLock.unlock();
        }
    }

    @Override
    public final void start() {
        this.lifecycleLock.lock();
        try {
            if (!this.running) {
                this.doStart();
                this.running = true;
                if (log.isInfoEnabled()) {
                    log.info("started " + this);
                } else {
                    if(log.isDebugEnabled()) {
                        log.debug("already started " + this);                    
                    }
                }
            }
        } finally {
            this.lifecycleLock.unlock();
        }
    }

    @Override
    public final void stop() {
        this.lifecycleLock.lock();
        try {
            if (this.running) {
                this.doStop();
                this.running = false;
                if (log.isInfoEnabled()) {
                    log.info("stopped " + this);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("already stopped " + this);                    
                }
            }
        } finally {
            this.lifecycleLock.unlock();
        }
    }

    @Override
    public final void stop(Runnable callback) {
        this.lifecycleLock.lock();
        try {
            this.stop();
            callback.run();
        } finally {
            this.lifecycleLock.unlock();
        }
    }

    /**
     * Sets the used {@link TaskScheduler}.
     * 
     * @param taskScheduler the task scheduler
     */
    public void setTaskScheduler(TaskScheduler taskScheduler) {
        Assert.notNull(taskScheduler, "taskScheduler must not be null");
        this.taskScheduler = taskScheduler;
    }

    /**
     * Gets the defined {@link TaskScheduler}.
     * 
     * @return the defined task scheduler
     */
    protected TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    /**
     * Sets the used {@link TaskExecutor}.
     * 
     * @param taskExecutor the task executor
     */
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        Assert.notNull(taskExecutor, "taskExecutor must not be null");
        this.taskExecutor = taskExecutor;
    }

    /**
     * Gets the defined {@link TaskExecutor}.
     * 
     * @return the defined task executor
     */
    protected TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    /**
     * Subclasses may implement this for initialization logic. Called
     * during the {@link InitializingBean} phase. Implementor should
     * always call super method not to break initialization chain.
     */
    protected void onInit() throws Exception {}

    /**
     * Subclasses must implement this method with the start behavior. This
     * method will be invoked while holding the {@link #lifecycleLock}.
     */
    protected abstract void doStart();

    /**
     * Subclasses must implement this method with the stop behavior. This method
     * will be invoked while holding the {@link #lifecycleLock}.
     */
    protected abstract void doStop();
    
}
