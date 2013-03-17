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

import java.util.List;

import org.apache.hadoop.yarn.api.records.LocalResourceType;
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.springframework.yarn.fs.LocalResourcesFactoryBean;
import org.springframework.yarn.fs.LocalResourcesFactoryBean.Entry;
import org.w3c.dom.Element;

/**
 * Simple namespace parser for yarn:localresources.
 *
 * @author Janne Valkealahti
 *
 */
public class LocalresourcesParser extends AbstractImprovedSimpleBeanDefinitionParser {

	public static final String DEFAULT_ID = "yarnLocalresources";

	@Override
	protected Class<?> getBeanClass(Element element) {
		return LocalResourcesFactoryBean.class;
	}

	@Override
	protected String defaultId(ParserContext context, Element element) {
		return DEFAULT_ID;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);

		ManagedList<BeanDefinition> entries = new ManagedList<BeanDefinition>();

		parseEntries(element, "hdfs", entries);
		builder.addPropertyValue("hdfsEntries", entries);
	}

	private void parseEntries(Element element, String name, List<BeanDefinition> entries) {
		List<Element> cp = DomUtils.getChildElementsByTagName(element, name);
		for (Element entry : cp) {
			BeanDefinitionBuilder bd = BeanDefinitionBuilder.genericBeanDefinition(Entry.class);

			bd.addConstructorArgValue(
					entry.hasAttribute("type") ?
					LocalResourceType.valueOf(entry.getAttribute("type").toUpperCase()) :
					null);

			bd.addConstructorArgValue(
					entry.hasAttribute("visibility") ?
					LocalResourceVisibility.valueOf(entry.getAttribute("visibility").toUpperCase()) :
					null);

			bd.addConstructorArgValue(entry.getAttribute("path"));

			// if set to null, factory will try to set defaults
			bd.addConstructorArgValue(entry.hasAttribute("local") ? entry.getAttribute("local") : null);
			bd.addConstructorArgValue(entry.hasAttribute("remote") ? entry.getAttribute("remote") : null);
			entries.add(bd.getBeanDefinition());
		}
	}

}
