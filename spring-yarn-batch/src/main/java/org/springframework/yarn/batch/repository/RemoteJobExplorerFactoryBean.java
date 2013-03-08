package org.springframework.yarn.batch.repository;

import org.springframework.batch.core.explore.support.AbstractJobExplorerFactoryBean;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * A {@link FactoryBean} that automates the creation of a
 * {@link SimpleJobExplorer} using in-memory DAO implementations.
 * 
 * @author Janne Valkealahti
 * 
 */
public class RemoteJobExplorerFactoryBean extends AbstractJobExplorerFactoryBean implements InitializingBean {

    private RemoteJobRepositoryFactoryBean repositoryFactory;

    /**
     * Create an instance with the provided
     * {@link RemoteJobRepositoryFactoryBean} as a source of Dao instances.
     * 
     * @param repositoryFactory
     */
    public RemoteJobExplorerFactoryBean(RemoteJobRepositoryFactoryBean repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    /**
     * Create a factory with no {@link RemoteJobRepositoryFactoryBean}. It must
     * be injected as a property.
     */
    public RemoteJobExplorerFactoryBean() {
    }

    /**
     * The repository factory that can be used to create daos for the explorer.
     * 
     * @param repositoryFactory
     *            a {@link RemoteJobExplorerFactoryBean}
     */
    public void setRepositoryFactory(RemoteJobRepositoryFactoryBean repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    /**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.state(repositoryFactory != null, "A RemoteJobRepositoryFactoryBean must be provided");
        repositoryFactory.afterPropertiesSet();
    }

    @Override
    protected JobExecutionDao createJobExecutionDao() throws Exception {
        return repositoryFactory.getJobExecutionDao();
    }

    @Override
    protected JobInstanceDao createJobInstanceDao() throws Exception {
        return repositoryFactory.getJobInstanceDao();
    }

    @Override
    protected StepExecutionDao createStepExecutionDao() throws Exception {
        return repositoryFactory.getStepExecutionDao();
    }

    @Override
    protected ExecutionContextDao createExecutionContextDao() throws Exception {
        return repositoryFactory.getExecutionContextDao();
    }

    public Object getObject() throws Exception {
        return new SimpleJobExplorer(createJobInstanceDao(), createJobExecutionDao(), createStepExecutionDao(),
                createExecutionContextDao());
    }

}
