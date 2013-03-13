package org.springframework.yarn.integration.config;

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.yarn.config.YarnNamespaceUtils;

/**
 * Simple namespace parser for &lt;yarn-int:converter&gt;.
 * 
 * @author Oleg Zhurakousky
 * @author Mark Fisher
 * @author Janne Valkealahti
 * 
 */
public class IntegrationConverterParser extends AbstractBeanDefinitionParser {

    private final ManagedSet<Object> converters = new ManagedSet<Object>();

    private volatile boolean initialized;

    private final Object initializationMonitor = new Object();

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        this.initializeConversionServiceInfrastructureIfNecessary(parserContext);
        BeanComponentDefinition converterDefinition = YarnNamespaceUtils.parseInnerHandlerDefinition(element,
                parserContext);
        if (converterDefinition != null) {
            this.converters.add(converterDefinition);
        } else {
            String beanName = element.getAttribute("ref");
            Assert.isTrue(StringUtils.hasText(beanName),
                    "Either a 'ref' attribute pointing to a Converter or a <bean> sub-element defining a Converter is required.");
            this.converters.add(new RuntimeBeanReference(beanName));
        }
        return null;
    }

    private void initializeConversionServiceInfrastructureIfNecessary(ParserContext parserContext) {
        synchronized (this.initializationMonitor) {
            if (!this.initialized) {
                String contextPackage = "org.springframework.yarn.integration.convert.";
                BeanDefinitionBuilder creatorBuilder = BeanDefinitionBuilder.rootBeanDefinition(contextPackage
                        + "ConversionServiceCreator");
                BeanDefinitionReaderUtils.registerWithGeneratedName(creatorBuilder.getBeanDefinition(),
                        parserContext.getRegistry());
                BeanDefinitionBuilder conversionServiceBuilder = BeanDefinitionBuilder
                        .rootBeanDefinition(contextPackage + "ConverterRegistrar");
                conversionServiceBuilder.addConstructorArgValue(converters);
                BeanDefinitionReaderUtils.registerWithGeneratedName(conversionServiceBuilder.getBeanDefinition(),
                        parserContext.getRegistry());
                this.initialized = true;
            }
        }
    }

}
