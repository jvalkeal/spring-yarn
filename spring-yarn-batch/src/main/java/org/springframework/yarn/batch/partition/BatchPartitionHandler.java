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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.StepExecutionSplitter;
import org.springframework.yarn.batch.am.BatchAppmaster;

/**
 * Implementation for {@link PartitionHandler} used with {@link BatchAppmaster}.
 *
 * @author Janne Valkealahti
 *
 */
public class BatchPartitionHandler implements PartitionHandler {

	private static final Log log = LogFactory.getLog(BatchPartitionHandler.class);

	/** Remote step name to execute */
	private String stepName;

	/** Grid size for partitioning */
	private int gridSize = 1;

	/** Handle back to our app master */
	private BatchAppmaster master;

	/**
	 * Constructs a partition handler with a given app master.
	 *
	 * @param master the app master
	 */
	public BatchPartitionHandler(BatchAppmaster master, int gridSize, String stepName) {
		this.master = master;
		this.gridSize = gridSize;
		this.stepName = stepName;
	}

	@Override
	public Collection<StepExecution> handle(StepExecutionSplitter stepSplitter, StepExecution stepExecution)
			throws Exception {

		Set<StepExecution> split = stepSplitter.split(stepExecution, gridSize);

		if(log.isDebugEnabled()) {
			log.debug("Created " + split.size() + " splits for stepName=" + stepName);
			for(StepExecution execution : split) {
				log.debug("Splitted step execution: " + execution);
			}
		}
		master.addStepSplits(stepName, split);

		master.getMonitor().setTotal(gridSize);
		master.getAllocator().allocateContainers(gridSize);

		Collection<StepExecution> result = new ArrayList<StepExecution>();

		// monitor
		for(int i = 0; i<30; i++) {
			try {
				if(master.getMonitor().isCompleted()) {
					log.debug("got complete from monitor");
					break;
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				log.info("sleep error", e);
			}
		}
		log.debug("setting completed:");
		master.getMonitor().setCompleted();

		// for now just return empty collection,
		// should create a service hook to receive
		// responses from remote containers
		return result;
	}

	/**
	 * Passed to the {@link StepExecutionSplitter} in the
	 * {@link #handle(StepExecutionSplitter, StepExecution)} method, instructing
	 * it how many {@link StepExecution} instances are required, ideally. The
	 * {@link StepExecutionSplitter} is allowed to ignore the grid size in the
	 * case of a restart, since the input data partitions must be preserved.
	 *
	 * @param gridSize the number of step executions that will be created
	 */
	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}

	/**
	 * The name of the {@link Step} that will be used to execute the partitioned
	 * {@link StepExecution}. This is a regular Spring Batch step, with all the
	 * business logic required to complete an execution based on the input
	 * parameters in its {@link StepExecution} context. The name will be
	 * translated into a {@link Step} instance by the remote worker.
	 *
	 * @param stepName the name of the {@link Step} instance to execute business logic
	 */
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

}
