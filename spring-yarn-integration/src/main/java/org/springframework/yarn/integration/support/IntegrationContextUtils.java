package org.springframework.yarn.integration.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;

/**
 * Utility methods for accessing common integration components from the BeanFactory.
 * 
 * @author Janne Valkealahti
 *
 */
public class IntegrationContextUtils {

    public static final String TASK_SCHEDULER_BEAN_NAME = "taskScheduler";
    public static final String YARN_INTEGRATION_CONVERSION_SERVICE_BEAN_NAME = "yarnIntegrationConversionService";

    /**
     * Return the {@link TaskScheduler} bean whose name is "taskScheduler" if
     * available.
     * 
     * @param beanFactory BeanFactory for lookup, must not be null.
     */
    public static TaskScheduler getTaskScheduler(BeanFactory beanFactory) {
        return getBeanOfType(beanFactory, TASK_SCHEDULER_BEAN_NAME, TaskScheduler.class);
    }

    /**
     * Return the {@link TaskScheduler} bean whose name is "taskScheduler".
     * 
     * @param beanFactory BeanFactory for lookup, must not be null.
     * @throws IllegalStateException if no such bean is available
     */
    public static TaskScheduler getRequiredTaskScheduler(BeanFactory beanFactory) {
        TaskScheduler taskScheduler = getTaskScheduler(beanFactory);
        Assert.state(taskScheduler != null, "No such bean '" + TASK_SCHEDULER_BEAN_NAME + "'");
        return taskScheduler;
    }

    /**
     * Return the {@link ConversionService} bean whose name is
     * "integrationConversionService" if available.
     * 
     * @param beanFactory BeanFactory for lookup, must not be null.
     */
    public static ConversionService getConversionService(BeanFactory beanFactory) {
        return getBeanOfType(beanFactory, YARN_INTEGRATION_CONVERSION_SERVICE_BEAN_NAME, ConversionService.class);
    }

    private static <T> T getBeanOfType(BeanFactory beanFactory, String beanName, Class<T> type) {
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        if (!beanFactory.containsBean(beanName)) {
            return null;
        }
        return beanFactory.getBean(beanName, type);
    }

}
