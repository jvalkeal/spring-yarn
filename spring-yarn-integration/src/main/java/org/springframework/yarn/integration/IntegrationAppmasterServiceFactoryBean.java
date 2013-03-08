package org.springframework.yarn.integration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.yarn.am.AppmasterService;
import org.springframework.yarn.integration.ip.mind.MindAppmasterService;

public class IntegrationAppmasterServiceFactoryBean implements InitializingBean, FactoryBean<AppmasterService> {

    private AppmasterService service;
    
    @Override
    public AppmasterService getObject() throws Exception {
        return service;
    }

    @Override
    public Class<AppmasterService> getObjectType() {
        return AppmasterService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        service = new MindAppmasterService();
    }


}
