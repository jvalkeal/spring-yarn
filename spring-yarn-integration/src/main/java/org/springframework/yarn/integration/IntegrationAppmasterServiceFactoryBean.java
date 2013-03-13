package org.springframework.yarn.integration;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.yarn.am.AppmasterService;
import org.springframework.yarn.integration.ip.mind.MindAppmasterService;
import org.springframework.yarn.integration.support.PortExposingTcpSocketSupport;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Bean factory for building {@link AppmasterService} instances
 * supported by this module.
 * 
 * @author Janne Valkealahti
 *
 */
public class IntegrationAppmasterServiceFactoryBean implements FactoryBean<AppmasterService>,
        InitializingBean, DisposableBean, BeanFactoryAware {

    /** Service returned from this factory */
    private AppmasterService appmasterService;

    /** Service side message dispatching */
    private SubscribableChannel messageChannel;    
    
    /** Socket support for listen socket bind operations */
    private PortExposingTcpSocketSupport socketSupport;    
    
    /** Implementation class of the service */
    private Class<AppmasterService> serviceImpl;
    
    /** Instance if service given as bean reference */
    private IntegrationAppmasterService<?> serviceRef;
    
    /** Possible jackson object mapper */
    private ObjectMapper objectMapper;

    /** Bean factory of this instance*/
    private BeanFactory beanFactory;
    
    @Override
    public AppmasterService getObject() throws Exception {
        return appmasterService;
    }

    @Override
    public Class<AppmasterService> getObjectType() {
        return AppmasterService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(!(serviceRef == null && serviceImpl == null),
                "Both serviceRef and serviceImpl must not be null");
        Assert.isTrue(!(serviceRef != null && serviceImpl != null),
                "Either serviceRef or serviceImpl can be defined");
        
        if(serviceRef != null) {
            appmasterService = serviceRef;
        } else if (serviceImpl != null && ClassUtils.isAssignable(AppmasterService.class, serviceImpl)) {
            appmasterService = BeanUtils.instantiateClass(serviceImpl);
        } else {
            throw new IllegalArgumentException("serviceImpl [" + serviceImpl + "] not assignable for AppmasterService");
        }
        
        if(appmasterService instanceof IntegrationAppmasterService) {
            ((IntegrationAppmasterService)appmasterService).setMessageChannel(messageChannel);
            ((IntegrationAppmasterService)appmasterService).setSocketSupport(socketSupport);
        }

        if(appmasterService instanceof MindAppmasterService) {
            ((MindAppmasterService)appmasterService).setObjectMapper(objectMapper);
        }

        if(appmasterService instanceof SmartLifecycle) {
            ((SmartLifecycle)appmasterService).start();
        }

        if(beanFactory != null && appmasterService instanceof BeanFactoryAware) {
            ((BeanFactoryAware)appmasterService).setBeanFactory(beanFactory);
        }
        
    }

    @Override
    public void destroy() throws Exception {
        if(appmasterService instanceof SmartLifecycle) {
            ((SmartLifecycle)appmasterService).stop();
        }        
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    
    /**
     * Sets the message channel for service dispatching.
     * 
     * @param messageChannel the message channel
     */
    public void setChannel(SubscribableChannel messageChannel) {
        this.messageChannel = messageChannel;
    }
        
    /**
     * Sets the socket support to be used to get
     * port info from a server socket.
     * 
     * @param socketSupport the socket support
     */
    public void setSocketSupport(PortExposingTcpSocketSupport socketSupport) {
        this.socketSupport = socketSupport;
    }
    
    /**
     * Set the class implementing appmaster service.
     * 
     * @param serviceImpl the implementing class
     */
    public void setServiceImpl(Class<AppmasterService> serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    /**
     * Sets the appmaster service as an instance. This is used if
     * service is created externally and we just inject it into
     * this factory.
     * 
     * @param serviceRef the service instance
     */
    public void setServiceRef(IntegrationAppmasterService<?> serviceRef) {
        this.serviceRef = serviceRef;
    }

    /**
     * Sets the jackson object mapper.
     * 
     * @param objectMapper the jackson object mapper
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
