package org.springframework.yarn.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.springframework.yarn.am.AppmasterFactoryBean;
import org.springframework.yarn.support.ParsingUtils;
import org.w3c.dom.Element;

/**
 * Simple namespace parser for yarn:master.
 * 
 * @author Janne Valkealahti
 * 
 */
public class MasterParser extends AbstractSingleBeanDefinitionParser {

    public static final String DEFAULT_ID = "yarnAppmaster";
    
    @Override
    protected Class<?> getBeanClass(Element element) {
        return AppmasterFactoryBean.class;
    }
    
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        super.doParse(element, builder);

        List<Element> cp = DomUtils.getChildElementsByTagName(element, "container-command");
        for (Element entry : cp) {            
            String textContent = entry.getTextContent();
            String command = ParsingUtils.extractRunnableCommand(textContent);
            List<String> commands = new ArrayList<String>();
            commands.add(command);
            builder.addPropertyValue("commands", commands);
        }

        // adding references using fallback to default bean names        
        String attr = element.getAttribute("resourcelocalizer-ref");
        builder.addPropertyReference("resourceLocalizer", (StringUtils.hasText(attr) ? attr : "yarnLocalresources"));

        attr = element.getAttribute("configuration-ref");
        builder.addPropertyReference("configuration", (StringUtils.hasText(attr) ? attr : "yarnConfiguration"));

        attr = element.getAttribute("environment-ref");
        builder.addPropertyReference("environment", (StringUtils.hasText(attr) ? attr : "yarnEnvironment"));
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
