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

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.launch.AbstractCommandLineRunner;

/**
 * Simple launcher for application master.
 *
 * @author Janne Valkealahti
 *
 */
public class CommandLineAppmasterRunner extends AbstractCommandLineRunner<YarnAppmaster>{

	private static final Log log = LogFactory.getLog(CommandLineAppmasterRunner.class);

	@Override
	protected void handleBeanRun(YarnAppmaster bean, String[] parameters, Set<String> opts) {
		Properties properties = StringUtils.splitArrayElementsIntoProperties(parameters, "=");
		bean.setParameters(properties != null ? properties : new Properties());
		if(log.isDebugEnabled()) {
			log.debug("Starting YarnAppmaster bean: " + StringUtils.arrayToCommaDelimitedString(parameters));
		}
		bean.submitApplication();
		if(log.isDebugEnabled()) {
			log.debug("Waiting YarnAppmaster bean");
		}
		bean.waitForCompletion();
		if(log.isDebugEnabled()) {
			log.debug("Waiting YarnAppmaster bean done");
		}
	}

	@Override
	protected String getDefaultBeanIdentifier() {
		return YarnSystemConstants.DEFAULT_ID_APPMASTER;
	}

	@Override
	protected List<String> getValidOpts() {
		return null;
	}

	public static void main(String[] args) {
		new CommandLineAppmasterRunner().doMain(args);
	}

}
