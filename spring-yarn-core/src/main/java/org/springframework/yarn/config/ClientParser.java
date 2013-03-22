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
package org.springframework.yarn.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.client.YarnClientFactoryBean;
import org.springframework.yarn.support.ParsingUtils;
import org.w3c.dom.Element;

/**
 * Simple namespace parser for yarn:client.
 *
 * @author Janne Valkealahti
 *
 */
public class ClientParser extends AbstractSingleBeanDefinitionParser {

	public static final String DEFAULT_ID = "yarnClient";

	@Override
	protected Class<?> getBeanClass(Element element) {
		return YarnClientFactoryBean.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		super.doParse(element, builder);

		YarnNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "template");
		YarnNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "configuration", YarnSystemConstants.DEFAULT_ID_CONFIGURATION);
		YarnNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "environment", YarnSystemConstants.DEFAULT_ID_ENVIRONMENT);
		YarnNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "resource-localizer", YarnSystemConstants.DEFAULT_ID_LOCAL_RESOURCES);

		// parsing command needed for master
		List<Element> entries = DomUtils.getChildElementsByTagName(element, "master-command");
		for (Element entry : entries) {
			String textContent = entry.getTextContent();
			String command = ParsingUtils.extractRunnableCommand(textContent);
			List<String> commands = new ArrayList<String>();
			commands.add(command);
			builder.addPropertyValue("commands", commands);
		}
	}

	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
			throws BeanDefinitionStoreException {
		String name = super.resolveId(element, definition, parserContext);
		if (!StringUtils.hasText(name)) {
			name = DEFAULT_ID;
		}
		return name;
	}

}
