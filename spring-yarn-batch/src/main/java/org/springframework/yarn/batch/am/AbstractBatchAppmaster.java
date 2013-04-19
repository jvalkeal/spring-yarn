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
package org.springframework.yarn.batch.am;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.apache.hadoop.yarn.api.records.ResourceRequest;
import org.apache.hadoop.yarn.util.RackResolver;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.am.AbstractEventingAppmaster;
import org.springframework.yarn.am.AppmasterService;
import org.springframework.yarn.am.ContainerLauncherInterceptor;
import org.springframework.yarn.am.container.AbstractLauncher;
import org.springframework.yarn.am.container.ContainerRequestData;
import org.springframework.yarn.batch.event.PartitionedStepExecutionEvent;
import org.springframework.yarn.batch.listener.CompositePartitionedStepExecutionStateListener;
import org.springframework.yarn.batch.listener.PartitionedStepExecutionStateListener;
import org.springframework.yarn.batch.listener.PartitionedStepExecutionStateListener.PartitionedStepExecutionState;

/**
 * Base application master for running batch jobs.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractBatchAppmaster extends AbstractEventingAppmaster implements ContainerLauncherInterceptor {

	private static final Log log = LogFactory.getLog(AbstractBatchAppmaster.class);

	/** Batch job launcher */
	private JobLauncher jobLauncher;

	/** Name of the job to run */
	private String jobName;

	/** Step executions as reported back from containers */
	private List<StepExecution> stepExecutions = new ArrayList<StepExecution>();

	private Map<String, StepExecution> hostMapping = new LinkedHashMap<String, StepExecution>();
	private Map<String, StepExecution> rackMapping = new LinkedHashMap<String, StepExecution>();

	private Map<StepExecution, ContainerRequestData> requestData = new LinkedHashMap<StepExecution, ContainerRequestData>();
	private Map<StepExecution, Set<StepExecution>> masterExecutions = new HashMap<StepExecution, Set<StepExecution>>();
	private Map<StepExecution, String> remoteStepNames = new HashMap<StepExecution, String>();
	private Map<ContainerId, StepExecution> containerToStepMap = new HashMap<ContainerId, StepExecution>();

	/** Converter for job parameters  */
	private JobParametersConverter jobParametersConverter = new DefaultJobParametersConverter();

	/** Listener for partitioned step execution statuses */
	private CompositePartitionedStepExecutionStateListener stepExecutionStateListener =
			new CompositePartitionedStepExecutionStateListener();

	@Override
	protected void onInit() throws Exception {
		super.onInit();
		if(getLauncher() instanceof AbstractLauncher) {
			((AbstractLauncher)getLauncher()).addInterceptor(this);
		}
		RackResolver.init(getConfiguration());
	}

	@Override
	protected void onContainerAllocated(Container container) {
		if (log.isDebugEnabled()) {
			log.debug("Container allocated: " + container);
		}

		StepExecution stepExecution = null;

		String host = container.getNodeId().getHost();
		String rack = RackResolver.resolve(host).getNetworkLocation();
		if (log.isDebugEnabled()) {
			log.debug("Matching agains: host=" + host + " rack=" + rack);
		}


		Iterator<Entry<StepExecution, ContainerRequestData>> iterator = requestData.entrySet().iterator();
		while (iterator.hasNext() && stepExecution != null) {
			Entry<StepExecution, ContainerRequestData> entry = iterator.next();
			if (entry.getValue() != null && entry.getValue().getHosts() != null) {
				for (String h : entry.getValue().getHosts()) {
					if (h.equals(host)) {
						stepExecution = entry.getKey();
						break;
					}
				}
			}
		}

		log.debug("stepExecution after hosts match: " + stepExecution);

		iterator = requestData.entrySet().iterator();
		while (iterator.hasNext() && stepExecution != null) {
			Entry<StepExecution, ContainerRequestData> entry = iterator.next();
			if (entry.getValue() != null && entry.getValue().getRacks() != null) {
				for (String r : entry.getValue().getRacks()) {
					if (r.equals(rack)) {
						stepExecution = entry.getKey();
						break;
					}
				}
			}
		}

		log.debug("stepExecution after racks match: " + stepExecution);

		try {
			if (stepExecution == null) {
				stepExecution = requestData.entrySet().iterator().next().getKey();
			}
			requestData.remove(stepExecution);
			containerToStepMap.put(container.getId(), stepExecution);
			getLauncher().launchContainer(container, getCommands());
		} catch (NoSuchElementException e) {
			log.error("We didn't have step execution in request map.", e);
		}
	}

	@Override
	protected void onContainerLaunched(Container container) {
		if (log.isDebugEnabled()) {
			log.debug("Container launched: " + container);
		}
	}

	@Override
	protected void onContainerCompleted(ContainerStatus status) {
		super.onContainerCompleted(status);

		ContainerId containerId = status.getContainerId();
		StepExecution stepExecution = containerToStepMap.get(containerId);

		for (Entry<StepExecution, Set<StepExecution>> entry : masterExecutions.entrySet()) {
			Set<StepExecution> value = entry.getValue();
			if (value.remove(stepExecution)) {
				masterExecutions.put(entry.getKey(), value);
			}
			if (value.size() == 0) {
				getYarnEventPublisher().publishEvent(new PartitionedStepExecutionEvent(this, entry.getKey()));
				stepExecutionStateListener.state(PartitionedStepExecutionState.COMPLETED, entry.getKey());
				// TODO: this is wrong. we don't actually know if we
				// are complete. other partitioned steps may come later
				getMonitor().setCompleted();
				notifyCompleted();
			}
		}

		getAllocator().releaseContainer(containerId);
	}

	@Override
	public ContainerLaunchContext preLaunch(ContainerLaunchContext context) {
		AppmasterService service = getAppmasterService();

		if(log.isDebugEnabled()) {
			log.debug("Intercept launch context: " + context);
		}

		StepExecution stepExecution = containerToStepMap.get(context.getContainerId());
		String jobName = remoteStepNames.get(stepExecution);

		if(service != null) {
			int port = service.getPort();
			String address = service.getHost();
			Map<String, String> env = new HashMap<String, String>(context.getEnvironment());
			env.put(YarnSystemConstants.AMSERVICE_PORT, Integer.toString(port));
			env.put(YarnSystemConstants.AMSERVICE_HOST, address);
			env.put(YarnSystemConstants.AMSERVICE_BATCH_STEPNAME, jobName);
			env.put(YarnSystemConstants.AMSERVICE_BATCH_JOBEXECUTIONID, Long.toString(stepExecution.getJobExecutionId()));
			env.put(YarnSystemConstants.AMSERVICE_BATCH_STEPEXECUTIONID, Long.toString(stepExecution.getId()));
			context.setEnvironment(env);
			return context;
		} else {
			return context;
		}
	}

	/**
	 * Adds the partitioned step execution state listener.
	 *
	 * @param listener the listener
	 */
	public void addPartitionedStepExecutionStateListener(PartitionedStepExecutionStateListener listener) {
		stepExecutionStateListener.register(listener);
	}

	/**
	 * Gets the job launcher.
	 *
	 * @return the job launcher
	 */
	public JobLauncher getJobLauncher() {
		return jobLauncher;
	}

	/**
	 * Sets the job launcher.
	 *
	 * @param jobLauncher the new job launcher
	 */
	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	/**
	 * Gets the job name.
	 *
	 * @return the job name
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * Sets the job name.
	 *
	 * @param jobName the new job name
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * Gets the step executions.
	 *
	 * @return the step executions
	 */
	public List<StepExecution> getStepExecutions() {
		return stepExecutions;
	}


	/**
	 * Adds the step splits.
	 *
	 * @param masterStepExecution the partitioned steps parent step execution
	 * @param remoteStepName the remote step name
	 * @param stepExecutions the step executions splits
	 * @param resourceRequests the request data for step executions
	 */
	public void addStepSplits(StepExecution masterStepExecution, String remoteStepName,
			Set<StepExecution> stepExecutions, Map<StepExecution, ContainerRequestData> resourceRequests) {

		List<ResourceRequest> requests = new ArrayList<ResourceRequest>();
		for (Entry<StepExecution, ContainerRequestData> entry : resourceRequests.entrySet()) {
			StepExecution se = entry.getKey();
			ContainerRequestData crd = entry.getValue();

			requestData.put(se, crd);
			remoteStepNames.put(se, remoteStepName);

			ResourceRequest request = null;
			for (String host : crd.getHosts()) {
				hostMapping.put(host, se);
				request = Records.newRecord(ResourceRequest.class);
				request.setHostName(host);
				request.setNumContainers(1);
				request.setPriority(crd.getPriority());
				request.setCapability(crd.getCapability());
				requests.add(request);
			}
			for (String rack : crd.getRacks()) {
				rackMapping.put(rack, se);
				request = Records.newRecord(ResourceRequest.class);
				request.setHostName(rack);
				request.setNumContainers(1);
				request.setPriority(crd.getPriority());
				request.setCapability(crd.getCapability());
				requests.add(request);
			}

			request = Records.newRecord(ResourceRequest.class);
			request.setHostName("*");
			request.setNumContainers(stepExecutions.size());
			request.setPriority(crd.getPriority());
			request.setCapability(crd.getCapability());
			requests.add(request);

		}
		masterExecutions.put(masterStepExecution, stepExecutions);

		int remaining = stepExecutions.size() - resourceRequests.size();
		for (StepExecution execution : stepExecutions) {
			if (!requestData.containsKey(execution)) {
				requestData.put(execution, null);
			}
			if (!remoteStepNames.containsKey(execution)) {
				remoteStepNames.put(execution, remoteStepName);
			}
		}

		getAllocator().allocateContainers(remaining);
		getAllocator().allocateContainers(requests);
		getMonitor().setTotal(requests.size());
	}

	/**
	 * Gets the job parameters converter.
	 *
	 * @return the job parameters converter
	 */
	public JobParametersConverter getJobParametersConverter() {
		return jobParametersConverter;
	}

	/**
	 * Injection setter for {@link JobParametersConverter}.
	 *
	 * @param jobParametersConverter
	 */
	public void setJobParametersConverter(JobParametersConverter jobParametersConverter) {
		this.jobParametersConverter = jobParametersConverter;
	}

}
