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
package org.springframework.yarn.batch.partition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.Priority;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.StepExecutionSplitter;
import org.springframework.yarn.am.container.ContainerRequestData;
import org.springframework.yarn.batch.am.AbstractBatchAppmaster;
import org.springframework.yarn.support.compat.ResourceCompat;

/**
 * Implementation of Spring Batch {@link PartitionHandler} which does
 * partitioning based on number of input files from HDFS.
 *
 * @author Janne Valkealahti
 *
 */
public class HdfsSplitBatchPartitionHandler extends AbstractBatchPartitionHandler {

	private static final Log log = LogFactory.getLog(HdfsSplitBatchPartitionHandler.class);

	/** Yarn configuration */
	private Configuration configuration;

	/**
	 * Instantiates a new hdfs split batch partition handler.
	 *
	 * @param batchAppmaster the batch appmaster
	 */
	public HdfsSplitBatchPartitionHandler(AbstractBatchAppmaster batchAppmaster) {
		this(batchAppmaster, null);
	}

	/**
	 * Instantiates a new hdfs split batch partition handler.
	 *
	 * @param batchAppmaster the batch appmaster
	 */
	public HdfsSplitBatchPartitionHandler(AbstractBatchAppmaster batchAppmaster, Configuration configuration) {
		super(batchAppmaster);
		this.configuration = configuration;
	}

	/**
	 * Gets the Yarn configuration.
	 *
	 * @return the Yarn configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Sets the Yarn configuration.
	 *
	 * @param configuration the new Yarn configuration
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	protected Set<StepExecution> createStepExecutionSplits(StepExecutionSplitter stepSplitter, StepExecution stepExecution)
			throws Exception {

		String input = stepExecution.getJobParameters().getString("input");
		log.info("Input is " + input);

		FileSystem fs = FileSystem.get(configuration);
		Path path = new Path(input);
		FileStatus[] fileStatuses = fs.globStatus(path);

		Set<StepExecution> split = stepSplitter.split(stepExecution, fileStatuses.length);
		return split;
	}

	@Override
	protected Map<StepExecution, ContainerRequestData> createResourceRequestData(Set<StepExecution> stepExecutions) throws Exception {
		Map<StepExecution, ContainerRequestData> requests = new HashMap<StepExecution, ContainerRequestData>();

		for (StepExecution execution : stepExecutions) {
			Resource capability = Records.newRecord(Resource.class);
			capability.setMemory(64);
			ResourceCompat.setVirtualCores(capability, 1);
			Priority priority = Records.newRecord(Priority.class);
			priority.setPriority(0);

			FileSystem fs = FileSystem.get(configuration);
			Path path = new Path(execution.getExecutionContext().getString("fileName"));
			BlockLocation[] fileBlockLocations = fs.getFileBlockLocations(path, 0, 1);
			for (BlockLocation blockLocation : fileBlockLocations) {
				log.debug("block: " + blockLocation);
			}

			String[] hosts = fileBlockLocations[0].getHosts();
			String[] racks = new String[]{"default-rack"};
			requests.put(execution, new ContainerRequestData(execution, capability, hosts, racks, priority));
		}

		return requests;
	}

}
