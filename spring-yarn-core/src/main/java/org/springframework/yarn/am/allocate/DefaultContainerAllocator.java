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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.protocolrecords.AllocateRequest;
import org.apache.hadoop.yarn.api.protocolrecords.AllocateResponse;
import org.apache.hadoop.yarn.api.records.AMResponse;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.apache.hadoop.yarn.api.records.Priority;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.api.records.ResourceRequest;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.util.Assert;
import org.springframework.yarn.listener.CompositeContainerAllocatorListener;
import org.springframework.yarn.listener.ContainerAllocatorListener;

/**
 * Default allocator which polls resource manager, requests new containers
 * and acts as a heart beat sender at the same time.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultContainerAllocator extends AbstractPollingAllocator implements ContainerAllocator {

	private static final Log log = LogFactory.getLog(DefaultContainerAllocator.class);

	/** Listener dispatcher for events */
	private CompositeContainerAllocatorListener allocatorListener = new CompositeContainerAllocatorListener();

	/** Counter of current requested containers */
	private AtomicInteger numRequestedContainers = new AtomicInteger();

	/** Container request priority */
	private int priority = 0;

	/** Desired allocation location */
	private String hostname = "*";

	/** Resource capability as of cores */
	private int virtualcores = 1;

	/** Resource capability as of memory */
	private int memory = 64;

	@Override
	protected void onInit() throws Exception {
		super.onInit();
		Assert.notNull(getApplicationAttemptId(), "ApplicationAttemptId must be set");
	}

	@Override
	public void allocateContainers(int count) {
		// adding new container allocation count, poller
		// will pick it up later
		int newCount = numRequestedContainers.addAndGet(count);
		if(log.isDebugEnabled()) {
			log.debug("Adding " + count + " new containers. New count is " + newCount + " object:" + this);
		}
	}

	@Override
	public void addListener(ContainerAllocatorListener listener) {
		allocatorListener.register(listener);
	}

	@Override
	protected AMResponse doContainerRequest() {
		int count = numRequestedContainers.getAndSet(0);

		if(log.isDebugEnabled()) {
			log.debug("Requesting " + count + " new containers. " + " object:" + this);
		}

		List<ResourceRequest> requestedContainers = new ArrayList<ResourceRequest>();
		if(count > 0) {
			ResourceRequest containerAsk = getContainerResourceRequest(count);
			requestedContainers.add(containerAsk);
		}

		AllocateRequest request = Records.newRecord(AllocateRequest.class);
		request.setResponseId(11);
		request.setApplicationAttemptId(getApplicationAttemptId());
		request.addAllAsks(requestedContainers);
		// we don't release anything here
		request.addAllReleases(new ArrayList<ContainerId>());
		request.setProgress(0.5f);

		AllocateResponse allocate = getRmTemplate().allocate(request);
		return allocate.getAMResponse();
	}

	@Override
	protected void handleAllocatedContainers(List<Container> containers) {
		allocatorListener.allocated(containers);
	}

	@Override
	protected void handleCompletedContainers(List<ContainerStatus> containerStatuses) {
		allocatorListener.completed(containerStatuses);
	}

	/**
	 * Gets the priority for container request.
	 *
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Sets the priority for container request.
	 *
	 * @param priority the new priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Gets the hostname for container request.
	 *
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Sets the hostname for container request defining <em>host/rack</em> on
	 * which the allocation is desired. A special value of <em>*</em> signifies
	 * that <em>any</em> host/rack is acceptable.
	 *
	 * @param hostname the new hostname
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * Gets the virtualcores for container request.
	 *
	 * @return the virtualcores
	 */
	public int getVirtualcores() {
		return virtualcores;
	}

	/**
	 * Sets the virtualcores for container request defining
	 * <em>number of virtual cpu cores</em> of the resource.
	 *
	 * @param virtualcores the new virtualcores
	 */
	public void setVirtualcores(int virtualcores) {
		this.virtualcores = virtualcores;
	}

	/**
	 * Gets the memory for container request.
	 *
	 * @return the memory
	 */
	public int getMemory() {
		return memory;
	}

	/**
	 * Sets the memory for container request defining
	 * <em>memory</em> of the resource.
	 *
	 * @param memory the new memory
	 */
	public void setMemory(int memory) {
		this.memory = memory;
	}

	/**
	 * Creates a request to allocate new containers.
	 *
	 * @param numContainers number of containers to request
	 * @return request to be sent to resource manager
	 */
	private ResourceRequest getContainerResourceRequest(int numContainers) {
		ResourceRequest request = Records.newRecord(ResourceRequest.class);
		request.setHostName(hostname);
		request.setNumContainers(numContainers);
		Priority pri = Records.newRecord(Priority.class);
		pri.setPriority(priority);
		request.setPriority(pri);
		Resource capability = Records.newRecord(Resource.class);
		capability.setMemory(memory);
		request.setCapability(capability);
		return request;
	}

}
