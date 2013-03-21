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
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.am.StaticAppmaster;
import org.springframework.yarn.am.allocate.DefaultContainerAllocator;
import org.springframework.yarn.support.ParsingUtils;
import org.w3c.dom.Element;

/**
 * Simple namespace parser for yarn:master.
 *
 * @author Janne Valkealahti
 *
 */
public class MasterParser extends AbstractBeanDefinitionParser {

	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {

		// for now, defaulting to StaticAppmaster
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(StaticAppmaster.class);

		List<Element> cp = DomUtils.getChildElementsByTagName(element, "container-command");
		for (Element entry : cp) {
			String textContent = entry.getTextContent();
			String command = ParsingUtils.extractRunnableCommand(textContent);
			List<String> commands = new ArrayList<String>();
			commands.add(command);
			builder.addPropertyValue("commands", commands);
		}

		// allocator - for now, defaulting to DefaultContainerAllocator
		BeanDefinitionBuilder defBuilder = BeanDefinitionBuilder.genericBeanDefinition(DefaultContainerAllocator.class);
		defBuilder.addPropertyReference("configuration", "yarnConfiguration");
		Element allocElement = DomUtils.getChildElementByTagName(element, "container-allocator");
		YarnNamespaceUtils.setReferenceIfAttributeDefined(defBuilder, element, "environment", "yarnEnvironment");
		if(allocElement != null) {
			YarnNamespaceUtils.setValueIfAttributeDefined(defBuilder, allocElement, "hostname");
			YarnNamespaceUtils.setValueIfAttributeDefined(defBuilder, allocElement, "virtualcores");
			YarnNamespaceUtils.setValueIfAttributeDefined(defBuilder, allocElement, "memory");
			YarnNamespaceUtils.setValueIfAttributeDefined(defBuilder, allocElement, "priority");
		}
		AbstractBeanDefinition beanDef = defBuilder.getBeanDefinition();
		String beanName = BeanDefinitionReaderUtils.generateBeanName(beanDef, parserContext.getRegistry());
		parserContext.registerBeanComponent(new BeanComponentDefinition(beanDef, beanName));
		builder.addPropertyReference("allocator", beanName);

		YarnNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "resource-localizer", "yarnLocalresources");
		YarnNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "configuration", "yarnConfiguration");
		YarnNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "environment", "yarnEnvironment");

		return builder.getBeanDefinition();
	}

	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
			throws BeanDefinitionStoreException {
		String name = super.resolveId(element, definition, parserContext);
		if (!StringUtils.hasText(name)) {
			name = YarnSystemConstants.DEFAULT_ID_APPMASTER;
		}
		return name;
	}

}
