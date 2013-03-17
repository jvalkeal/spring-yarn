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
package org.springframework.yarn.am.monitor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.ContainerStatus;

public class DefaultContainerMonitor implements ContainerMonitor {

	private static final Log log = LogFactory.getLog(DefaultContainerMonitor.class);

	private int total = -1;
	private int completed = 0;

	public DefaultContainerMonitor() {
	}

	@Override
	public void monitorContainer(List<ContainerStatus> completedContainers) {
		completed += completedContainers.size();
	}

	@Override
	public void setTotal(int count) {
		total = count;
	}

	@Override
	public boolean isCompleted() {
		log.debug("complete: completed=" + completed + " total=" + total);
		return completed >= total;
	}

	@Override
	public void setCompleted() {
		completed = total;
	}

}
