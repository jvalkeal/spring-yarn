package org.springframework.yarn.integration.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.yarn.support.LifecycleObjectSupport;

/**
 * Extension of {@link LifecycleObjectSupport} adding common
 * {@link ConversionService} for all implementors of this class.
 * 
 * @author Janne Valkealahti
 *
 */
public abstract class IntegrationObjectSupport extends LifecycleObjectSupport {

    private static final Log log = LogFactory.getLog(IntegrationObjectSupport.class);

    private volatile ConversionService conversionService;

    /**
     * Gets the spring conversion service.
     * 
     * @return the conversion service
     */
    protected final ConversionService getConversionService() {
        if (this.conversionService == null && getBeanFactory() != null) {
            this.conversionService = IntegrationContextUtils.getConversionService(getBeanFactory());
            if (this.conversionService == null && log.isDebugEnabled()) {
                log.debug("Unable to attempt conversion of Message types. Component '" + this
                        + "' has no explicit ConversionService reference, "
                        + "and there is no 'yarnIntegrationConversionService' bean within the context.");
            }
        }
        return this.conversionService;
    }

    /**
     * Sets the {@link ConversionService}.
     * 
     * @param conversionService the conversion service
     */
    protected void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

}
