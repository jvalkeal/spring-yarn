package org.springframework.yarn.integration.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Handler for 'yarn:int' namespace. All element parsers will be registered
 * here.
 * 
 * @author Janne Valkealahti
 * 
 */
public class IntegrationNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("amservice", new AmServiceParser());
    }

}
