package org.springframework.yarn.integration;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.PollableChannel;
import org.springframework.util.ClassUtils;
import org.springframework.yarn.am.AppmasterServiceClient;
import org.springframework.yarn.integration.ip.mind.MindAppmasterServiceClient;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Factory which creates appmaster service clients.
 * 
 * @author Janne Valkealahti
 *
 */
public class IntegrationAppmasterServiceClientFactoryBean implements FactoryBean<AppmasterServiceClient>,
        InitializingBean, BeanFactoryAware {

    /** Appmaster service serviceClient returned by this factory */
    private AppmasterServiceClient serviceClient;
   
    /** Implementation class of the service */
    private Class<AppmasterServiceClient> serviceImpl;
    
    /** Outbound request channel */
    private MessageChannel requestChannel;
    
    /** Inbound response channel */
    private PollableChannel responseChannel;
    
    /** Possible jackson object mapper */
    private ObjectMapper objectMapper;
    
    /** Bean factory of this instance*/
    private BeanFactory beanFactory;
    
    @Override
    public AppmasterServiceClient getObject() throws Exception {
        return serviceClient;
    }

    @Override
    public Class<AppmasterServiceClient> getObjectType() {
        return AppmasterServiceClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void afterPropertiesSet() throws Exception {        
        if(ClassUtils.isAssignable(AppmasterServiceClient.class, serviceImpl)) {
            serviceClient = BeanUtils.instantiateClass(serviceImpl);
        }
        
        if(serviceClient instanceof MindAppmasterServiceClient) {
            ((MindAppmasterServiceClient)serviceClient).setObjectMapper(objectMapper);
        }

        if(serviceClient instanceof IntegrationAppmasterServiceClient) {
            ((IntegrationAppmasterServiceClient)serviceClient).setRequestChannel(requestChannel);
            ((IntegrationAppmasterServiceClient)serviceClient).setResponseChannel(responseChannel);
        }
        
        if(beanFactory != null && serviceClient instanceof BeanFactoryAware) {
            ((BeanFactoryAware)serviceClient).setBeanFactory(beanFactory);
        }
    }
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Set the class implementing appmaster service client.
     * 
     * @param serviceImpl the implementing class
     */
    public void setServiceImpl(Class<AppmasterServiceClient> serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    /**
     * Sets the outbount request channel.
     * 
     * @param requestChannel the request channel
     */
    public void setRequestChannel(MessageChannel requestChannel) {
        this.requestChannel = requestChannel;
    }

    /**
     * Sets the inbound response channel.
     * 
     * @param responseChannel the response channel
     */
    public void setResponseChannel(PollableChannel responseChannel) {
        this.responseChannel = responseChannel;
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
