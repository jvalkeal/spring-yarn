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

        String attr = element.getAttribute("template-ref");
        if (StringUtils.hasText(attr)) {
            builder.addPropertyValue("template", new RuntimeBeanReference(attr));
        }

        // adding references using fallback to default bean names        
        attr = element.getAttribute("resourcelocalizer-ref");
        builder.addPropertyReference("resourceLocalizer", (StringUtils.hasText(attr) ? attr : "yarnLocalresources"));

        attr = element.getAttribute("configuration-ref");
        builder.addPropertyReference("configuration", (StringUtils.hasText(attr) ? attr : "yarnConfiguration"));

        attr = element.getAttribute("environment-ref");
        builder.addPropertyReference("environment", (StringUtils.hasText(attr) ? attr : "yarnEnvironment"));
        
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
