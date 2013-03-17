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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.springframework.yarn.config.ClientParser;
import org.springframework.yarn.launch.AbstractCommandLineRunner;

/**
 *
 * java CommandLineClientRunner <contextConfig> <beanName>
 * java CommandLineClientRunner <contextConfig> <beanName> -submit key1=val1
 * java CommandLineClientRunner <contextConfig> <beanName> -list key1=val1
 *
 * @author Janne Valkealahti
 *
 */
public class CommandLineClientRunner extends AbstractCommandLineRunner<YarnClient> {

	private static final Log log = LogFactory.getLog(CommandLineClientRunner.class);

	private static List<String> validOpts = new ArrayList<String>(1);

	static {
		validOpts.add("-list");
	}

	public CommandLineClientRunner() {
	}

	@Override
	protected void handleBeanRun(YarnClient bean, String[] parameters, Set<String> opts) {
		if(opts.contains("-list")) {
			print(bean.listApplications());
		} else {
			bean.submitApplication();
		}
	}

	@Override
	protected String getDefaultBeanIdentifier() {
		return ClientParser.DEFAULT_ID;
	}

	@Override
	protected List<String> getValidOpts() {
		return validOpts;
	}

	private void print(List<ApplicationReport> applications) {
		log.info("Listing applications:");
		StringBuilder buf = new StringBuilder();
		for(ApplicationReport a : applications) {
			buf.append(a.getName());
			buf.append('\t');
			buf.append(a.getApplicationId());
			buf.append('\n');
		}
		System.out.println(buf.toString());
	}

	public static void main(String[] args) {
		new CommandLineClientRunner().doMain(args);
	}

}
