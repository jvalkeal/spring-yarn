package org.springframework.yarn.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Handler for 'yarn' namespace. All element parsers will be
 * registered here.
 * 
 * @author Janne Valkealahti
 *
 */
public class YarnNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("client", new ClientParser());
        registerBeanDefinitionParser("master", new MasterParser());
        registerBeanDefinitionParser("configuration", new YarnConfigParser());
        registerBeanDefinitionParser("localresources", new LocalresourcesParser());
        registerBeanDefinitionParser("environment", new EnvironmentParser());
    }

}
