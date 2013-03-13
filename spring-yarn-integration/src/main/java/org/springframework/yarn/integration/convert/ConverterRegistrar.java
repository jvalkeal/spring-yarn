package org.springframework.yarn.integration.convert;

import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.util.Assert;
import org.springframework.yarn.integration.support.IntegrationContextUtils;

/**
 * Utility class that keeps track of a set of Converters in order to register
 * them with the "integrationConversionService" upon initialization.
 * 
 * @author Oleg Zhurakousky
 * @author Mark Fisher
 * @author Janne Valkealahti
 * 
 */
class ConverterRegistrar implements InitializingBean, BeanFactoryAware {

    private final Set<Converter<?, ?>> converters;

    private BeanFactory beanFactory;

    public ConverterRegistrar(Set<Converter<?, ?>> converters) {
        this.converters = converters;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(beanFactory, "BeanFactory is required");
        ConversionService conversionService = IntegrationContextUtils.getConversionService(beanFactory);
        if (conversionService instanceof GenericConversionService) {
            ConversionServiceFactory.registerConverters(converters, (GenericConversionService) conversionService);
        } else {
            Assert.notNull(conversionService, "Failed to locate '"
                    + IntegrationContextUtils.YARN_INTEGRATION_CONVERSION_SERVICE_BEAN_NAME + "'");
        }
    }

}
