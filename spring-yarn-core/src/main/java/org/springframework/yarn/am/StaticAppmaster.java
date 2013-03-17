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
import org.springframework.context.SmartLifecycle;

/**
 * A simple application master implementation which will allocate
 * and launch a number of containers, monitor container statuses
 * and finally exit the application by sending corresponding
 * message back to resource manager.
 *
 * @author Janne Valkealahti
 *
 */
public class StaticAppmaster extends AbstractProcessingAppmaster implements YarnAppmaster {

	private static final Log log = LogFactory.getLog(StaticAppmaster.class);

	public StaticAppmaster() {
	}

	@Override
	public void submitApplication() {
		log.info("Submitting application");
		start();
		int count = Integer.parseInt(getParameters().getProperty(AppmasterConstants.CONTAINER_COUNT, "1"));
		log.info("count: " + count);
		getMonitor().setTotal(count);
		getAllocator().allocateContainers(count);
	}

	@Override
	public void waitForCompletion() {
		for(int i = 0; i<30; i++) {
			try {
				if(getMonitor().isCompleted()) {
					log.debug("got complete from monitor");
					break;
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				log.info("sleep error", e);
			}
		}
		stop();
	}

	@Override
	protected void doStart() {
		super.doStart();
		if(getAllocator() instanceof SmartLifecycle) {
			((SmartLifecycle)getAllocator()).start();
		}

		AppmasterService service = getAppmasterService();
		log.info("AppmasterService: " + service);

		if(getAppmasterService() instanceof SmartLifecycle) {
			((SmartLifecycle)getAppmasterService()).start();
		}

	}


}
