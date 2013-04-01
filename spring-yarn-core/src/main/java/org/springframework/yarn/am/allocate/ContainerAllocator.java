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

import org.springframework.yarn.listener.ContainerAllocatorListener;

/**
 * General interface for container allocators.
 *
 * @author Janne Valkealahti
 *
 */
public interface ContainerAllocator {

	/**
	 * Allocate new containers.
	 *
	 * @param count the new container count to allocate
	 */
	void allocateContainers(int count);

	/**
	 * Adds the {@link ContainerAllocatorListener}.
	 *
	 * @param listener the {@link ContainerAllocatorListener}
	 */
	void addListener(ContainerAllocatorListener listener);

	/**
	 * Sets the current progress of application.
	 *
	 * @param progress the current progress of application
	 */
	void setProgress(float progress);

}
