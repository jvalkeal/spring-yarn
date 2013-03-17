/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.yarn.am.allocate;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.AMResponse;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.util.Assert;
import org.springframework.yarn.YarnSystemException;

/**
 *
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractPollingAllocator extends AbstractAllocator {

	private static final Log log = LogFactory.getLog(AbstractAllocator.class);

	private volatile Executor taskExecutor = new SyncTaskExecutor();
	private volatile Trigger trigger = new PeriodicTrigger(1000);
	private volatile boolean initialized;
	private volatile Runnable poller;
	private volatile ScheduledFuture<?> runningTask;
	private final Object initializationMonitor = new Object();

	/**
	 * Default empty constructor.
	 */
	public AbstractPollingAllocator() {
	}

	/**
	 * Sets {@link Executor} used to run polling tasks.
	 *
	 * @param taskExecutor executor to set
	 */
	public void setTaskExecutor(Executor taskExecutor) {
		Assert.notNull(taskExecutor, "taskExecutor must not be null");
		this.taskExecutor = taskExecutor;
	}

	/**
	 * Sets {@link Trigger} used to trigger polling tasks.
	 *
	 * @param trigger trigger to set
	 */
	public void setTrigger(Trigger trigger) {
		Assert.notNull(trigger, "trigger must not be null");
		this.trigger = trigger;
	}

	@Override
	protected void onInit() {
		synchronized (initializationMonitor) {
			if (initialized) {
				return;
			}
			Assert.notNull(trigger, "Trigger is required");
			try {
				this.poller = this.createPoller();
				this.initialized = true;
			}
			catch (Exception e) {
				throw new YarnSystemException("Failed to create Poller", e);
			}
		}
	}

	@Override
	protected void doStart() {
		if (!this.initialized) {
			this.onInit();
		}
		Assert.state(getTaskScheduler() != null, "unable to start polling, no taskScheduler available");
		this.runningTask = getTaskScheduler().schedule(this.poller, this.trigger);
	}

	@Override
	protected void doStop() {
		if (this.runningTask != null) {
			this.runningTask.cancel(true);
		}
		this.runningTask = null;
		this.initialized = false;
	}

	/**
	 * Subclasses needs to implements this method to do container
	 * requests against resource manager. This method is called
	 * during the polling cycle handled by this class. New containers
	 * and newly released containers are passed to methods
	 * {@link #handleAllocatedContainers(List)} and
	 * {@link #handleCompletedContainers(List)}.
	 *
	 * @return {@link AMResponse} from a resource manager
	 */
	protected abstract AMResponse doContainerRequest();

	/**
	 * Subclasses needs to implement this method to handle newly
	 * allocated containers.
	 *
	 * @param containers list of newly allocate containers
	 */
	protected abstract void handleAllocatedContainers(List<Container> containers);

	/**
	 * Subclasses needs to implement this method to handle newly
	 * released containers.
	 *
	 * @param containerStatuses list of newly released containers
	 */
	protected abstract void handleCompletedContainers(List<ContainerStatus> containerStatuses);

	private Runnable createPoller() throws Exception {
		Callable<Boolean> pollingTask = new Callable<Boolean>() {
			public Boolean call() throws Exception {
				return doPoll();
			}
		};
		return new Poller(pollingTask);
	}

	private boolean doPoll() {
		boolean result = false;

		if (log.isDebugEnabled()){
			log.debug("polling new/completed containers");
		}

		AMResponse response = doContainerRequest();

		List<Container> allocatedContainers = response.getAllocatedContainers();
		if(allocatedContainers != null && allocatedContainers.size() > 0) {
			if (log.isDebugEnabled()){
				log.debug("got " + allocatedContainers.size() + " new containers");
			}
			handleAllocatedContainers(allocatedContainers);
			result = true;
		}

		List<ContainerStatus> containerStatuses = response.getCompletedContainersStatuses();
		if(containerStatuses != null && containerStatuses.size() > 0) {
			if (log.isDebugEnabled()){
				log.debug("got " + containerStatuses.size() + " completed containers");
			}
			handleCompletedContainers(containerStatuses);
			result = true;
		}

		return result;
	}

	private class Poller implements Runnable {

		private final Callable<Boolean> pollingTask;

		public Poller(Callable<Boolean> pollingTask) {
			this.pollingTask = pollingTask;
		}

		public void run() {
			taskExecutor.execute(new Runnable() {
				public void run() {
					try {
						// TODO: we could use boolean return value to do
						//       something productive like throttling polls
						pollingTask.call();
					} catch (Exception e) {
						throw new RuntimeException("Error executing polling task", e);
					}
				}
			});
		}
	}

}
