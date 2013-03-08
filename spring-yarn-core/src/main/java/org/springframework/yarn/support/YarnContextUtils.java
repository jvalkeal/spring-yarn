package org.springframework.yarn.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;
import org.springframework.yarn.am.AppmasterService;

/**
 * Utility methods for accessing common components from the BeanFactory.
 * 
 * @author Janne Valkealahti
 * 
 */
public class YarnContextUtils {

    /** Default task scheduler bean name */
    public static final String TASK_SCHEDULER_BEAN_NAME = "taskScheduler";

    /** Default app master client service bean name */
    public static final String TASK_AMSERVER_BEAN_NAME = "yarnAmservice";
    
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
     * Return the {@link AppmasterService} bean whose name is "yarnAmserver" if
     * available.
     * 
     * @param beanFactory BeanFactory for lookup, must not be null.
     */
    public static AppmasterService getAppmasterService(BeanFactory beanFactory) {
        return getBeanOfType(beanFactory, TASK_AMSERVER_BEAN_NAME, AppmasterService.class);        
    }

    /**
     * Gets a bean from a factory with a given name and type.
     * 
     * @param beanFactory the bean factory
     * @param beanName the bean name
     * @param type the type as of a class
     * @return Bean known to a bean factory, null if not found.
     */
    private static <T> T getBeanOfType(BeanFactory beanFactory, String beanName, Class<T> type) {
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        if (!beanFactory.containsBean(beanName)) {
            return null;
        }
        return beanFactory.getBean(beanName, type);
    }
    
}
