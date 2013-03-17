package org.springframework.yarn.batch.repository;

import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.support.AbstractJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;
import org.springframework.yarn.integration.ip.mind.AppmasterMindScOperations;

public class RemoteJobRepositoryFactoryBean extends AbstractJobRepositoryFactoryBean {

    private RemoteJobExecutionDao jobExecutionDao;

    private RemoteJobInstanceDao jobInstanceDao;

    private RemoteStepExecutionDao stepExecutionDao;

    private RemoteExecutionContextDao executionContextDao;
    
    private AppmasterMindScOperations appmasterScOperations;

    public RemoteJobRepositoryFactoryBean() {
        this(new ResourcelessTransactionManager());
    }
    
    public JobExecutionDao getJobExecutionDao() {
        return jobExecutionDao;
    }

    public JobInstanceDao getJobInstanceDao() {
        return jobInstanceDao;
    }

    public StepExecutionDao getStepExecutionDao() {
        return stepExecutionDao;
    }

    public ExecutionContextDao getExecutionContextDao() {
        return executionContextDao;
    }    
    
    public RemoteJobRepositoryFactoryBean(PlatformTransactionManager transactionManager) {
        setTransactionManager(transactionManager);
    }

    public void setAppmasterScOperations(AppmasterMindScOperations appmasterScOperations) {
        this.appmasterScOperations = appmasterScOperations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.notNull(appmasterScOperations, "AppmasterScOperations must not be null.");        
    }

    @Override
    protected JobInstanceDao createJobInstanceDao() throws Exception {
        jobInstanceDao = new RemoteJobInstanceDao(appmasterScOperations);
        return jobInstanceDao;
    }

    @Override
    protected JobExecutionDao createJobExecutionDao() throws Exception {
        jobExecutionDao = new RemoteJobExecutionDao(appmasterScOperations);
        return jobExecutionDao;
    }

    @Override
    protected StepExecutionDao createStepExecutionDao() throws Exception {
        stepExecutionDao = new RemoteStepExecutionDao(appmasterScOperations);
        return stepExecutionDao;
    }

    @Override
    protected ExecutionContextDao createExecutionContextDao() throws Exception {
        executionContextDao = new RemoteExecutionContextDao(appmasterScOperations);
        return executionContextDao;
    }

}
