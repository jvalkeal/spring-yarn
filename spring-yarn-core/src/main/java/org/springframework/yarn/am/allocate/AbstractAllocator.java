package org.springframework.yarn.am.allocate;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.util.Assert;

/**
 * The base class for Container allocator implementations.
 * 
 * <p>
 * This class implements {@link SmartLifecycle} and provides an
 * {@link #autoStartup} property. If <code>true</code>, the allocator will start
 * automatically upon initialization. Otherwise, it will require an explicit
 * invocation of its {@link #start()} method. The default value is
 * <code>true</code>. To require explicit startup, provide a value of
 * <code>false</code> to the {@link #setAutoStartup(boolean)} method.
 * 
 * @author Janne Valkealahti
 * 
 */
public abstract class AbstractAllocator implements SmartLifecycle, InitializingBean {

    private static final Log log = LogFactory.getLog(AbstractAllocator.class);

    // fields for lifecycle
    private volatile boolean autoStartup = true;
    private volatile int phase = 0;
    private volatile boolean running;

    // lock to protect lifycycle methods
    private final ReentrantLock lifecycleLock = new ReentrantLock();

    private TaskScheduler taskScheduler = new ConcurrentTaskScheduler();

    public AbstractAllocator() {
    }

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

    /**
     * Sets whether this instance should participate automatically
     * with application context's life cycle methods. 
     * 
     * @param autoStartup autostartup to set
     */
    public final void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
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

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        Assert.notNull(taskScheduler, "taskScheduler must not be null");
        this.taskScheduler = taskScheduler;
    }
    
    protected TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    /**
     * Subclasses may implement this for initialization logic. Called
     * during the {@link InitializingBean} phase.
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
