package org.springframework.yarn.integration.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.yarn.integration.IntegrationAppmasterServiceFactoryBean;
import org.w3c.dom.Element;

/**
 * Simple namespace parser for yarn:amserver.
 * 
 * @author Janne Valkealahti
 * 
 */
public class AmServiceParser extends AbstractSingleBeanDefinitionParser {

    public static final String DEFAULT_ID = "yarnAmservice";    
    
    @Override
    protected Class<?> getBeanClass(Element element) {
        return IntegrationAppmasterServiceFactoryBean.class;
    }
    
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        super.doParse(element, builder);

//        String attr = element.getAttribute("template-ref");
//        builder.addPropertyValue("template", new RuntimeBeanReference(attr));
//        attr = element.getAttribute("resourcelocalizer-ref");
//        builder.addPropertyReference("resourceLocalizer", (StringUtils.hasText(attr) ? attr : "yarnLocalresources"));

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
