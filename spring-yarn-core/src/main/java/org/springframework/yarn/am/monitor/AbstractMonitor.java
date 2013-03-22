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

import org.springframework.yarn.listener.CompositeContainerMonitorStateListener;
import org.springframework.yarn.listener.ContainerMonitorListener;
import org.springframework.yarn.listener.ContainerMonitorListener.ContainerMonitorState;
import org.springframework.yarn.support.LifecycleObjectSupport;

/**
 * The base class for Container monitoring implementations.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractMonitor extends LifecycleObjectSupport {

	/** Handler for state listener */
	private CompositeContainerMonitorStateListener stateListener =
			new CompositeContainerMonitorStateListener();

	/**
	 * Adds the container monitor state listener.
	 *
	 * @param listener the listener
	 */
	public void addContainerMonitorStateListener(ContainerMonitorListener listener) {
		stateListener.register(listener);
	}

	/**
	 * Notify new {@link ContainerMonitorState} for registered listeners.
	 *
	 * @param state the {@link ContainerMonitorState}
	 */
	protected void notifyState(ContainerMonitorState state) {
		stateListener.state(state);
	}

}
