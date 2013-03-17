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
package org.springframework.yarn.batch.am;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.yarn.am.AppmasterRmOperations;
import org.springframework.yarn.am.AppmasterRmTemplate;
import org.springframework.yarn.am.YarnAppmaster;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * Factory bean building {@link YarnAppmaster} instances.
 *
 * @author Janne Valkealahti
 *
 */
public class BatchAppmasterFactoryBean implements InitializingBean, FactoryBean<YarnAppmaster>, BeanFactoryAware, ApplicationContextAware {

	private Map<String, String> environment;
	private List<String> commands;
	private Configuration configuration;
	private YarnAppmaster master;
	private AppmasterRmOperations template;
	private ResourceLocalizer resourceLocalizer;
	private BeanFactory beanFactory;
	private JobLauncher jobLauncher;
	private String jobName;
	private ApplicationContext applicationContext;

	@Override
	public YarnAppmaster getObject() throws Exception {
		return master;
	}

	@Override
	public Class<YarnAppmaster> getObjectType() {
		return YarnAppmaster.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		if(template == null) {
			AppmasterRmTemplate armt = new AppmasterRmTemplate(configuration);
			armt.afterPropertiesSet();
			template = armt;
		}

		BatchAppmaster batchMaster = new BatchAppmaster();

		if (batchMaster instanceof BeanFactoryAware) {
			((BeanFactoryAware) batchMaster).setBeanFactory(beanFactory);
		}

		batchMaster.setConfiguration(configuration);
		batchMaster.setCommands(commands);
		batchMaster.setTemplate(template);
		batchMaster.setEnvironment(environment);
		batchMaster.setResourceLocalizer(resourceLocalizer);
		batchMaster.setJobLauncher(jobLauncher);
		batchMaster.setJobName(jobName);
		batchMaster.setApplicationContext(applicationContext);
		batchMaster.afterPropertiesSet();
		master = batchMaster;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setEnvironment(Map<String, String> environment) {
		this.environment = environment;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public void setTemplate(AppmasterRmOperations template) {
		this.template = template;
	}

	public void setResourceLocalizer(ResourceLocalizer resourceLocalizer) {
		this.resourceLocalizer = resourceLocalizer;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
