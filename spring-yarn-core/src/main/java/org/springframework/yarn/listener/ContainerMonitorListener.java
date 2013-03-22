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
package org.springframework.yarn.listener;

/**
 * Interface used for monitor to state of the monitor.
 *
 * @author Janne Valkealahti
 *
 */
public interface ContainerMonitorListener {

	/**
	 * Invoked when monitoring state is changes.
	 *
	 * @param state the {@link ContainerMonitorState}
	 */
	void state(ContainerMonitorState state);

	/**
	 * Class holding state info.
	 */
	public class ContainerMonitorState {
		private int total;
		private int completed;
		private double progress;
		public ContainerMonitorState(int total, int completed, double progress) {
			super();
			this.total = total;
			this.completed = completed;
			this.progress = progress;
		}
		public int getTotal() {
			return total;
		}
		public int getCompleted() {
			return completed;
		}
		public double getProgress() {
			return progress;
		}
		@Override
		public String toString() {
			return "ContainerMonitorState [total=" + total + ", completed="
					+ completed + ", progress=" + progress + "]";
		}
	}

}
