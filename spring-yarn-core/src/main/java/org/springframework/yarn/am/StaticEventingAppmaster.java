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
package org.springframework.yarn.am;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.springframework.yarn.am.allocate.AbstractAllocator;

/**
 *
 * @author Janne Valkealahti
 *
 */
public class StaticEventingAppmaster extends AbstractEventingAppmaster implements YarnAppmaster {

	private static final Log log = LogFactory.getLog(StaticEventingAppmaster.class);

	private int runCount;
	private int okCount;

	@Override
	public void submitApplication() {
		log.info("Submitting application");
		registerAppmaster();
		start();
		if(getAllocator() instanceof AbstractAllocator) {
			((AbstractAllocator)getAllocator()).setApplicationAttemptId(getApplicationAttemptId());
		}
		runCount = Integer.parseInt(getParameters().getProperty(AppmasterConstants.CONTAINER_COUNT, "1"));
		log.info("count: " + runCount);
		getMonitor().setTotal(runCount);
		getAllocator().allocateContainers(runCount);
	}

	@Override
	protected void onContainerAllocated(Container container) {
		getLauncher().launchContainer(container, getCommands());
	}

	@Override
	protected void onContainerLaunched(Container container) {
	}

	@Override
	protected void onContainerCompleted(ContainerStatus status) {
		super.onContainerCompleted(status);
		int exitStatus = status.getExitStatus();
		if(exitStatus == 0) {
			okCount++;
		} else {
			getAllocator().allocateContainers(1);
		}
		if(okCount >= runCount) {
			getMonitor().setCompleted();
			notifyCompleted();
		}
	}

}
