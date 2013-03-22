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
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.am.AbstractProcessingAppmaster;
import org.springframework.yarn.am.AppmasterService;
import org.springframework.yarn.am.YarnAppmaster;
import org.springframework.yarn.batch.repository.BatchAppmasterService;
import org.springframework.yarn.batch.repository.JobRepositoryRemoteServiceInterceptor;
import org.springframework.yarn.batch.repository.JobRepositoryRpcFactory;
import org.springframework.yarn.batch.repository.bindings.PartitionedStepExecutionStatusReq;
import org.springframework.yarn.batch.repository.bindings.PartitionedStepExecutionStatusRes;
import org.springframework.yarn.batch.repository.bindings.StepExecutionType;
import org.springframework.yarn.integration.ip.mind.binding.BaseObject;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Implementation of application master which can be used to
 * run Spring Batch jobs on Hadoop Yarn cluster.
 * <p>
 * Application master will act as a context running the Spring
 * Batch job. Order to make some sense in terms of using cluster
 * resources, job itself should be able to partition itself in
 * a way that Yarn containers can be used to split the load.
 *
 * @author Janne Valkealahti
 *
 */
public class BatchAppmaster extends AbstractProcessingAppmaster implements YarnAppmaster, Partitioner {

	private static final Log log = LogFactory.getLog(BatchAppmaster.class);

	private JobLauncher jobLauncher;
	private String jobName;
	private ApplicationContext applicationContext;
	private Queue<SplitEntry> splitEntries = new ConcurrentLinkedQueue<BatchAppmaster.SplitEntry>();

	/** Step executions as reported back from containers */
	private List<StepExecution> stepExecutions = new ArrayList<StepExecution>();

	@Override
	public void submitApplication() {
		start();
		try {
			Job job = (Job) applicationContext.getBean(jobName);
			log.debug("launching job:" + job);
			jobLauncher.run(job, new JobParameters());
		} catch (Exception e) {
			log.error("Error running job", e);
		}
	}

//	@Override
//	public void waitForCompletion() {
//		for (int i = 0; i < 30; i++) {
//			try {
//				if (getMonitor().isCompleted()) {
//					log.debug("got complete from monitor");
//					break;
//				}
//				Thread.sleep(1000);
//			} catch (Exception e) {
//				log.info("sleep error", e);
//			}
//		}
//		log.debug("waiting latch done, doing stop");
//		stop();
//	}

	@Override
	protected void doStart() {
		super.doStart();

		AppmasterService service = getAppmasterService();
		if(log.isDebugEnabled() && service != null) {
			log.debug("We have a appmaster service " + service);
		}

		if(service instanceof BatchAppmasterService) {
			((BatchAppmasterService)service).addInterceptor(new JobRepositoryRemoteServiceInterceptor() {

				@Override
				public BaseObject preRequest(BaseObject baseObject) {
					log.info("got intercept type=" + baseObject.getType());
					if(baseObject.getType().equals("PartitionedStepExecutionStatusReq")) {
						StepExecutionType stepExecutionType = ((PartitionedStepExecutionStatusReq)baseObject).stepExecution;
						StepExecution convertStepExecution = JobRepositoryRpcFactory.convertStepExecutionType(stepExecutionType);
						stepExecutions.add(convertStepExecution);
						return null;
					} else {
						return baseObject;
					}
				}

				@Override
				public BaseResponseObject postRequest(BaseResponseObject baseResponseObject) {
					return baseResponseObject;
				}

				@Override
				public BaseResponseObject handleRequest(BaseObject baseObject) {
					return new PartitionedStepExecutionStatusRes();
				}
			});
		}

		if(service != null && service.hasPort()) {
			for(int i=0; i<10; i++) {
				if(service.getPort() == -1) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
				} else {
					break;
				}
				// should fail
			}
		}

		if(getAllocator() instanceof SmartLifecycle) {
			((SmartLifecycle)getAllocator()).start();
		}

		if(getAppmasterService() instanceof SmartLifecycle) {
			((SmartLifecycle)getAppmasterService()).start();
		}

	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> map = new HashMap<String, ExecutionContext>(gridSize);
		for (int i = 0; i < gridSize; i++) {
			map.put("partition" + i, new ExecutionContext());
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ContainerLaunchContext preLaunch(ContainerLaunchContext context) {
		AppmasterService service = getAppmasterService();

		if(log.isDebugEnabled()) {
			log.debug("Intercept launch context: " + context);
		}

		SplitEntry splitEntry = splitEntries.poll();

		if(service != null) {
			int port = service.getPort();
			String address = service.getHost();
			Map<String, String> env = new HashMap(context.getEnvironment());
			env.put(YarnSystemConstants.AMSERVICE_PORT, Integer.toString(port));
			env.put(YarnSystemConstants.AMSERVICE_HOST, address);
			env.put(YarnSystemConstants.AMSERVICE_BATCH_STEPNAME, splitEntry.stepName);
			env.put(YarnSystemConstants.AMSERVICE_BATCH_JOBEXECUTIONID, Long.toString(splitEntry.stepExecution.getJobExecutionId()));
			env.put(YarnSystemConstants.AMSERVICE_BATCH_STEPEXECUTIONID, Long.toString(splitEntry.stepExecution.getId()));
			context.setEnvironment(env);
			return context;
		} else {
			return super.preLaunch(context);
		}
	}

	public List<StepExecution> getStepExecutions() {
		return stepExecutions;
	}

	public void addStepSplits(String stepName, Set<StepExecution> stepSplits) {
		for(StepExecution stepExecution : stepSplits) {
			splitEntries.add(new SplitEntry(stepName, stepExecution));
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	private static class SplitEntry {
		public String stepName;
		public StepExecution stepExecution;
		public SplitEntry(String stepName, StepExecution stepExecution) {
			super();
			this.stepName = stepName;
			this.stepExecution = stepExecution;
		}
	}

}
