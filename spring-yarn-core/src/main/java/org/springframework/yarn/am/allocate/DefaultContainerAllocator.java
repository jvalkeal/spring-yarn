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
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
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
import org.apache.hadoop.yarn.util.BuilderUtils;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.util.Assert;
import org.springframework.yarn.listener.CompositeContainerAllocatorListener;
import org.springframework.yarn.listener.ContainerAllocatorListener;
import org.springframework.yarn.support.compat.ResourceCompat;

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

	/** Default hosts to schedule */
	private String[] hosts = new String[0];

	/** Default racks to schedule */
	private String[] racks = new String[0];

	/** Resource capability as of cores */
	private int virtualcores = 1;

	/** Resource capability as of memory */
	private int memory = 64;

	/** Increasing counter for rpc request id*/
	private AtomicInteger requestId = new AtomicInteger();

	/** Current progress reported during allocate requests */
	private float applicationProgress = 0;

	/** Queued container id's to be released */
	private Queue<ContainerId> releaseContainers = new ConcurrentLinkedQueue<ContainerId>();

	/**
	 * Map for resource requests. Resource request is kept in both key and value
	 * because we use comparator to get request by its priority, hostname and capability.
	 * This allows us to update the container count on a mapped value.
	 */
	private TreeMap<ResourceRequest, ResourceRequest> resourceRequests =
			new TreeMap<ResourceRequest, ResourceRequest>(new BuilderUtils.ResourceRequestComparator());

	@Override
	public void allocateContainers(int count) {
		// adding new container allocation count, poller
		// will pick it up later
		int newCount = numRequestedContainers.addAndGet(count);
		if(log.isDebugEnabled()) {
			log.debug("Adding " + count + " new containers. New count is " + newCount);
		}
	}

	@Override
	public void addListener(ContainerAllocatorListener listener) {
		allocatorListener.register(listener);
	}

	@Override
	public void allocateContainers(List<ResourceRequest> requests) {
		for (ResourceRequest request : requests) {
			synchronized (resourceRequests) {
				ResourceRequest resourceRequest = resourceRequests.get(request);
				if (resourceRequest != null) {
					int count = resourceRequest.getNumContainers() + request.getNumContainers();
					request.setNumContainers(count);
				}
				if(log.isDebugEnabled()) {
					log.debug("Adding new container using request=" + request);
				}
				resourceRequests.put(request, request);
			}
		}
	}

	@Override
	public void releaseContainers(List<Container> containers) {
		for (Container container : containers) {
			releaseContainer(container.getId());
		}
	}

	@Override
	public void releaseContainer(ContainerId containerId) {
		releaseContainers.add(containerId);
	}

	@Override
	protected AMResponse doContainerRequest() {
		int count = numRequestedContainers.getAndSet(0);

		List<ResourceRequest> requestedContainers = new ArrayList<ResourceRequest>();
		if(count > 0) {
			requestedContainers.addAll(buildResourceRequests(count));
			allocateContainers(requestedContainers);
			requestedContainers.clear();
		}

		synchronized (resourceRequests) {
			requestedContainers.addAll(resourceRequests.values());
			resourceRequests.clear();
		}

		if(log.isDebugEnabled()) {
			log.debug("Requesting containers using " +requestedContainers.size() + " requests.");
			for (ResourceRequest resourceRequest : requestedContainers) {
				log.debug("ResourceRequest: " + resourceRequest + " with count=" +
						resourceRequest.getNumContainers() + " with hostName=" + resourceRequest.getHostName());
			}
		}

		List<ContainerId> release = new ArrayList<ContainerId>();
		ContainerId element = null;
		while ((element = releaseContainers.poll()) != null) {
			release.add(element);
		}

		AllocateRequest request = Records.newRecord(AllocateRequest.class);
		request.setResponseId(requestId.get());
		request.setApplicationAttemptId(getApplicationAttemptId());
		request.addAllAsks(requestedContainers);
		request.addAllReleases(release);
		request.setProgress(applicationProgress);

		AllocateResponse allocate = getRmTemplate().allocate(request);
		requestId.set(allocate.getAMResponse().getResponseId());
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

	@Override
	public void setProgress(float progress) {
		applicationProgress = progress;
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
	 * Gets the hosts.
	 *
	 * @return the hosts
	 */
	public String[] getHosts() {
		return hosts;
	}

	/**
	 * Sets the hosts.
	 *
	 * @param hosts the new hosts
	 */
	public void setHosts(String[] hosts) {
		Assert.notNull(hosts, "Hosts array must not be null");
		this.hosts = hosts;
	}

	/**
	 * Gets the racks.
	 *
	 * @return the racks
	 */
	public String[] getRacks() {
		return racks;
	}

	/**
	 * Sets the racks.
	 *
	 * @param racks the new racks
	 */
	public void setRacks(String[] racks) {
		Assert.notNull(racks, "Racks array must not be null");
		this.racks = racks;
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
	 * Builds a list of resource requests.
	 *
	 * @param count the container count
	 * @return the list of {@link ResourceRequest}s
	 */
	private List<ResourceRequest> buildResourceRequests(int count) {
		List<ResourceRequest> requests = new ArrayList<ResourceRequest>();

		for (String host : getHosts()) {
			requests.add(getContainerResourceRequest(count, host));
		}

		for (String rack : getRacks()) {
			requests.add(getContainerResourceRequest(count, rack));
		}

		requests.add(getContainerResourceRequest(count, "*"));

		return requests;
	}

	/**
	 * Creates a request to allocate new containers.
	 *
	 * @param numContainers number of containers to request
	 * @return request to be sent to resource manager
	 */
	private ResourceRequest getContainerResourceRequest(int numContainers, String hostName) {
		ResourceRequest request = Records.newRecord(ResourceRequest.class);
		request.setHostName(hostName);
		request.setNumContainers(numContainers);
		Priority pri = Records.newRecord(Priority.class);
		pri.setPriority(priority);
		request.setPriority(pri);
		Resource capability = Records.newRecord(Resource.class);
		capability.setMemory(memory);
		ResourceCompat.setVirtualCores(capability, virtualcores);
		request.setCapability(capability);
		return request;
	}

}
