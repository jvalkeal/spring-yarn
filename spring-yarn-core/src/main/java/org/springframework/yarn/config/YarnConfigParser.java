package org.springframework.yarn.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.yarn.configuration.ConfigurationFactoryBean;
import org.w3c.dom.Element;

/**
 * Simple namespace parser for yarn:configuration.
 * 
 * @author Janne Valkealahti
 * 
 */
class YarnConfigParser extends AbstractPropertiesConfiguredBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ConfigurationFactoryBean.class;
    }

    @Override
    protected boolean isEligibleAttribute(String attributeName) {
        return (!"resources".equals(attributeName)) && super.isEligibleAttribute(attributeName);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);
        NamespaceUtils.setCSVProperty(element, builder, "resources");
    }

    @Override
    protected String defaultId(ParserContext context, Element element) {
        return "yarnConfiguration";
    }

}
