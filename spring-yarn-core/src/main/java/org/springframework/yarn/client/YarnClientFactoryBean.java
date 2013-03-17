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

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * Factory bean building {@link YarnClient} instances.
 *
 * @author Janne Valkealahti
 *
 */
public class YarnClientFactoryBean implements InitializingBean, FactoryBean<YarnClient> {

	private Map<String, String> environment;
	private List<String> commands;
	//private String queue;
	private ClientRmOperations template;
	private Configuration configuration;
	private ResourceLocalizer resourceLocalizer;
	private String appName = "";

	private CommandYarnClient client;

	@Override
	public void afterPropertiesSet() throws Exception {

		if(template == null) {
			ClientRmTemplate crmt = new ClientRmTemplate(configuration);
			crmt.afterPropertiesSet();
			template = crmt;
		}

		client = buildClient();
	}

	@Override
	public YarnClient getObject() throws Exception {
		return client;
	}

	@Override
	public Class<YarnClient> getObjectType() {
		return YarnClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setEnvironment(Map<String, String> environment) {
		this.environment = environment;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public void setTemplate(ClientRmOperations template) {
		this.template = template;
	}

	public void setResourceLocalizer(ResourceLocalizer resourceLocalizer) {
		this.resourceLocalizer = resourceLocalizer;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	private CommandYarnClient buildClient() {
		CommandYarnClient client = new CommandYarnClient(template);
		client.setCommands(commands);


		//Circular placeholder reference 'CLASSPATH' in property definitions
//      StringBuilder classPathEnv = new StringBuilder("${CLASSPATH}:./*");

//        Map<String, String> env = new HashMap<String, String>();
//        StringBuilder classPathEnv = new StringBuilder("./*");
//        for (String c : configuration.getStrings(YarnConfiguration.YARN_APPLICATION_CLASSPATH,
//                YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH)) {
//            classPathEnv.append(':');
//            classPathEnv.append(c.trim());
//        }
//        classPathEnv.append(":./log4j.properties");
//        env.put("CLASSPATH", classPathEnv.toString());
//        client.setEnvironment(env);

		client.setEnvironment(environment);
		client.setResourceLocalizer(resourceLocalizer);
		client.setAppName(appName);
		return client;
	}

}
