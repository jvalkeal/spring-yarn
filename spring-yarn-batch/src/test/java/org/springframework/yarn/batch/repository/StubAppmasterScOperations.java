package org.springframework.yarn.batch.repository;

import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.client.AppmasterScOperations;

/**
 * Faking appmaster service client operations for
 * batch job repository access using in-memory
 * map based repository.
 * 
 * @author Janne Valkealahti
 *
 */
public class StubAppmasterScOperations implements AppmasterScOperations {

    private JobExecutionDao jobExecutionDao;
    private JobInstanceDao jobInstanceDao;
    private StepExecutionDao stepExecutionDao;
    private ExecutionContextDao executionContextDao;
    
    JobRepositoryRemoteService jobRepositoryRemoteService;

    public StubAppmasterScOperations() {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
        try {
            factory.afterPropertiesSet();
            jobExecutionDao = factory.getJobExecutionDao();
            jobInstanceDao = factory.getJobInstanceDao();
            stepExecutionDao = factory.getStepExecutionDao();
            executionContextDao = factory.getExecutionContextDao();
            jobRepositoryRemoteService = new JobRepositoryRemoteService();
            jobRepositoryRemoteService.setJobExecutionDao(jobExecutionDao);
            jobRepositoryRemoteService.setJobInstanceDao(jobInstanceDao);
            jobRepositoryRemoteService.setStepExecutionDao(stepExecutionDao);
            jobRepositoryRemoteService.setExecutionContextDao(executionContextDao);
            jobRepositoryRemoteService.afterPropertiesSet();
        } catch (Exception e) {
        }
    }

    @Override
    public RpcMessage<?> get(RpcMessage<?> message) {
        return jobRepositoryRemoteService.get(message);
    }
    
}
