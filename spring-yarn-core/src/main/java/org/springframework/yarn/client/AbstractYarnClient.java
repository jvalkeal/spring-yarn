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
package org.springframework.yarn.client;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.api.records.Priority;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * Base implementation providing functionality for {@link YarnClient}.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractYarnClient implements YarnClient, InitializingBean {

	private ClientRmOperations clientRmOperations;

	private int priority;
	private String queue = "default";
	private ResourceLocalizer resourceLocalizer;
	private Map<String, String> environment;
	private List<String> commands;
	private String appName = "";

	public AbstractYarnClient(ClientRmOperations clientRmOperations) {
		super();
		this.clientRmOperations = clientRmOperations;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(clientRmOperations, "clientRmOperations can't be null");
	}

	@Override
	public void submitApplication() {
		resourceLocalizer.distribute();
		clientRmOperations.submitApplication(getSubmissionContext());
	}

	@Override
	public List<ApplicationReport> listApplications() {
		return clientRmOperations.listApplications();
	}

	/**
	 * Sets the {@link ClientRmOperations} implementation for
	 * accessing resource manager.
	 *
	 * @param clientRmOperations The client to resource manager implementation
	 */
	public void setClientRmOperations(ClientRmOperations clientRmOperations) {
		this.clientRmOperations = clientRmOperations;
	}

	public void setEnvironment(Map<String, String> environment) {
		this.environment = environment;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public void setResourceLocalizer(ResourceLocalizer resourceLocalizer) {
		this.resourceLocalizer = resourceLocalizer;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	protected ApplicationSubmissionContext getSubmissionContext() {
		ApplicationSubmissionContext context = Records.newRecord(ApplicationSubmissionContext.class);
		context.setApplicationId(clientRmOperations.getNewApplication().getApplicationId());
		context.setApplicationName(appName);
		context.setAMContainerSpec(getMasterContainerLaunchContext());
		Priority record = Records.newRecord(Priority.class);
		record.setPriority(priority);
		context.setPriority(record);
		context.setQueue(queue);
		return context;
	}

	protected ContainerLaunchContext getMasterContainerLaunchContext() {
		ContainerLaunchContext context = Records.newRecord(ContainerLaunchContext.class);
		context.setLocalResources(resourceLocalizer.getResources());
		context.setEnvironment(environment);
		context.setCommands(commands);
		Resource capability = Records.newRecord(Resource.class);
		capability.setMemory(100);
		context.setResource(capability);
		return context;
	}

}
