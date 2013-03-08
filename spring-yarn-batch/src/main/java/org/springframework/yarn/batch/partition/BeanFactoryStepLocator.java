package org.springframework.yarn.batch.partition;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.StepLocator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.Assert;

/**
 * A {@link StepLocator} implementation that just looks in its enclosing bean
 * factory for components of type {@link Step}.
 * 
 * @author Janne Valkealahti
 * 
 */
public class BeanFactoryStepLocator implements StepLocator, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Look up a bean with the provided name of type {@link Step}.
     * 
     * @see StepLocator#getStep(String)
     */
    @Override
    public Step getStep(String stepName) {
        return (Step) beanFactory.getBean(stepName, Step.class);
    }

    /**
     * Look in the bean factory for all beans of type {@link Step}.
     * 
     * @throws IllegalStateException
     *             if the {@link BeanFactory} is not listable
     * @see StepLocator#getStepNames()
     */
    @Override
    public Collection<String> getStepNames() {
        Assert.state(beanFactory instanceof ListableBeanFactory, "BeanFactory is not listable.");
        return Arrays.asList(((ListableBeanFactory) beanFactory).getBeanNamesForType(Step.class));
    }

}
