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
package org.springframework.yarn.am.container;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.StartContainerRequest;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.yarn.am.AppmasterCmTemplate;
import org.springframework.yarn.am.ContainerLauncherInterceptor;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 *
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultContainerLauncher implements ContainerLauncher {

	private static final Log log = LogFactory.getLog(DefaultContainerLauncher.class);

	private Configuration configuration;
	private ResourceLocalizer resourceLocalizer;
	private Map<String, String> environment;
	private ContainerLauncherInterceptor launcherInterceptor;

	public DefaultContainerLauncher(Configuration configuration, ResourceLocalizer resourceLocalizer, Map<String, String> environment) {
		this.configuration = configuration;
		this.resourceLocalizer = resourceLocalizer;
		this.environment = environment;
	}

	@Override
	public void launchContainer(Container container, List<String> commands) {

		AppmasterCmTemplate template = new AppmasterCmTemplate(configuration, container);

		try {
			template.afterPropertiesSet();
		} catch (Exception e) {
			log.error("error creating template", e);
		}

		ContainerLaunchContext ctx = Records.newRecord(ContainerLaunchContext.class);
		ctx.setContainerId(container.getId());
		ctx.setResource(container.getResource());
		String jobUserName = System.getenv(ApplicationConstants.Environment.USER.name());
		ctx.setUser(jobUserName);
		ctx.setLocalResources(resourceLocalizer.getResources());
		ctx.setCommands(commands);
		ctx.setEnvironment(environment);

		if(launcherInterceptor != null) {
			ctx = launcherInterceptor.preLaunch(ctx);
		}

		StartContainerRequest startReq = Records.newRecord(StartContainerRequest.class);
		startReq.setContainerLaunchContext(ctx);

		template.startContainer(startReq);
	}

	public void setResourceLocalizer(ResourceLocalizer resourceLocalizer) {
		this.resourceLocalizer = resourceLocalizer;
	}

	public void setContainerLauncherInterceptor(ContainerLauncherInterceptor launcherInterceptor) {
		this.launcherInterceptor = launcherInterceptor;
	}



}
