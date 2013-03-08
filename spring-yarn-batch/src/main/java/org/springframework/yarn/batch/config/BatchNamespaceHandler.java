package org.springframework.yarn.batch.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class BatchNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("master", new MasterParser());
    }

}
