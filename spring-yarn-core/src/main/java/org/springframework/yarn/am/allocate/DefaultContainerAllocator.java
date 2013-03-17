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
import org.apache.hadoop.yarn.api.records.ApplicationAttemptId;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.apache.hadoop.yarn.api.records.Priority;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.api.records.ResourceRequest;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.yarn.am.AppmasterRmOperations;
import org.springframework.yarn.listener.CompositeContainerAllocatorListener;

/**
 * Default allocator which polls resource manager, requests new containers
 * and acts as a heart beat sender at the same time.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultContainerAllocator extends AbstractPollingAllocator implements ContainerAllocator {

	private static final Log log = LogFactory.getLog(DefaultContainerAllocator.class);

	/** Operations template talking to resource manager */
	private AppmasterRmOperations rmTemplate;
	/** Current app attempt id */
	private ApplicationAttemptId appAttemptId;
	/** Listener dispatcher for events */
	private CompositeContainerAllocatorListener allocatorListener = new CompositeContainerAllocatorListener();
	/** Cache of allocated containers */
	//private List<Container> allocated;
	/** Counter of current requested containers */
	private AtomicInteger numRequestedContainers = new AtomicInteger();

	/**
	 * Constructs allocates using template and app attempt id.
	 *
	 * @param rmTemplate Template talking to resource manager
	 * @param appAttemptId app attempt id
	 */
	public DefaultContainerAllocator(AppmasterRmOperations rmTemplate, ApplicationAttemptId appAttemptId) {
		this.rmTemplate = rmTemplate;
		this.appAttemptId = appAttemptId;
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
			ResourceRequest containerAsk = setupContainerAskForRM(count);
			requestedContainers.add(containerAsk);
		}

		AllocateRequest request = Records.newRecord(AllocateRequest.class);
		request.setResponseId(11);
		request.setApplicationAttemptId(appAttemptId);
		request.addAllAsks(requestedContainers);
		// we don't release anything here
		request.addAllReleases(new ArrayList<ContainerId>());
		request.setProgress(0.5f);

		AllocateResponse allocate = rmTemplate.allocate(request);
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
	 * Creates a request to allocate new containers.
	 *
	 * @param numContainers number of containers to request
	 * @return request to be sent to resource manager
	 */
	private ResourceRequest setupContainerAskForRM(int numContainers) {
		ResourceRequest request = Records.newRecord(ResourceRequest.class);
		request.setHostName("*");
		request.setNumContainers(numContainers);
		Priority pri = Records.newRecord(Priority.class);
		pri.setPriority(0);
		request.setPriority(pri);
		Resource capability = Records.newRecord(Resource.class);
		capability.setMemory(10);
		request.setCapability(capability);
		return request;
	}

}
