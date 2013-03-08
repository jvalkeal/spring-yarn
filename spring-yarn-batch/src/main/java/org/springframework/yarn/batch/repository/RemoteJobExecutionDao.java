package org.springframework.yarn.batch.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.util.Assert;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.batch.repository.bindings.FindJobExecutionsRes;
import org.springframework.yarn.batch.repository.bindings.FindRunningJobExecutionsRes;
import org.springframework.yarn.batch.repository.bindings.GetJobExecutionRes;
import org.springframework.yarn.batch.repository.bindings.GetLastJobExecutionRes;
import org.springframework.yarn.batch.repository.bindings.JobExecutionType;
import org.springframework.yarn.batch.repository.bindings.SaveJobExecutionRes;
import org.springframework.yarn.batch.repository.bindings.SynchronizeStatusRes;
import org.springframework.yarn.batch.repository.bindings.UpdateJobExecutionRes;
import org.springframework.yarn.client.AppmasterScOperations;
import org.springframework.yarn.integration.ip.mind.MindRpcMessageHolder;

public class RemoteJobExecutionDao extends AbstractRemoteDao implements JobExecutionDao {

    public RemoteJobExecutionDao() {
        super();
    }

    public RemoteJobExecutionDao(AppmasterScOperations appmasterScOperations) {
        super(appmasterScOperations);
    }
    
    @Override
    public void saveJobExecution(JobExecution jobExecution) {
        validateJobExecution(jobExecution);
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildSaveJobExecutionReq(jobExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            SaveJobExecutionRes responseBody = JobRepositoryRpcFactory.convert(holder, SaveJobExecutionRes.class);
            jobExecution.setId(responseBody.id);
            jobExecution.setVersion(responseBody.version);
        } catch (Exception e) {
            throw convertException(e);
        }                
    }

    @Override
    public void updateJobExecution(JobExecution jobExecution) {
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildUpdateJobExecutionReq(jobExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            UpdateJobExecutionRes responseBody = JobRepositoryRpcFactory.convert(holder, UpdateJobExecutionRes.class);
        } catch (Exception e) {
            throw convertException(e);
        }                
    }

    @Override
    public List<JobExecution> findJobExecutions(JobInstance jobInstance) {
        List<JobExecution> jobExecutions = new ArrayList<JobExecution>();
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildFindJobExecutionsReq(jobInstance);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            FindJobExecutionsRes responseBody = JobRepositoryRpcFactory.convert(holder, FindJobExecutionsRes.class);
            for(JobExecutionType jobExecutionType : responseBody.jobExecutions) {
                jobExecutions.add(JobRepositoryRpcFactory.convertJobExecutionType(jobExecutionType));
            }
        } catch (Exception e) {
            throw convertException(e);
        }        
        return jobExecutions;
    }

    @Override
    public JobExecution getLastJobExecution(JobInstance jobInstance) {
        JobExecution jobExecution = null;
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildGetLastJobExecution(jobInstance);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            GetLastJobExecutionRes responseBody = JobRepositoryRpcFactory.convert(holder, GetLastJobExecutionRes.class);
            if(responseBody.jobExecution != null) {
                jobExecution = JobRepositoryRpcFactory.convertJobExecutionType(responseBody.jobExecution);
            }
        } catch (Exception e) {
            throw convertException(e);
        }        
        return jobExecution;
    }

    @Override
    public Set<JobExecution> findRunningJobExecutions(String jobName) {
        Set<JobExecution> jobExecutions = new HashSet<JobExecution>();
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildFindRunningJobExecutionsReq(jobName);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            FindRunningJobExecutionsRes responseBody = JobRepositoryRpcFactory.convert(holder, FindRunningJobExecutionsRes.class);
            for(JobExecutionType jobExecutionType : responseBody.jobExecutions) {
                jobExecutions.add(JobRepositoryRpcFactory.convertJobExecutionType(jobExecutionType));                
            }
        } catch (Exception e) {
            throw convertException(e);
        }        
        return jobExecutions;
    }

    @Override
    public JobExecution getJobExecution(Long executionId) {
        JobExecution jobExecution = null;
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildGetJobExecutionReq(executionId);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            GetJobExecutionRes responseBody = JobRepositoryRpcFactory.convert(holder, GetJobExecutionRes.class);            
            if(responseBody.jobExecution != null) {
                jobExecution = JobRepositoryRpcFactory.convertJobExecutionType(responseBody.jobExecution);
            }
        } catch (Exception e) {
            throw convertException(e);
        }
        return jobExecution;
    }

    @Override
    public void synchronizeStatus(JobExecution jobExecution) {
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildSynchronizeStatusReq(jobExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);            
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            SynchronizeStatusRes responseBody = JobRepositoryRpcFactory.convert(holder, SynchronizeStatusRes.class);            
            
            if(jobExecution.getVersion() != responseBody.version) {
                jobExecution.upgradeStatus(responseBody.status);
                jobExecution.setVersion(responseBody.version);
            }
            
        } catch (Exception e) {
            throw convertException(e);
        }                
    }
    
    /**
     * Validate JobExecution. At a minimum, JobId, StartTime, EndTime, and
     * Status cannot be null.
     *
     * @param jobExecution
     * @throws IllegalArgumentException
     */
    private void validateJobExecution(JobExecution jobExecution) {
        Assert.notNull(jobExecution);
        Assert.notNull(jobExecution.getJobId(), "JobExecution Job-Id cannot be null.");
        Assert.notNull(jobExecution.getStatus(), "JobExecution status cannot be null.");
        Assert.notNull(jobExecution.getCreateTime(), "JobExecution create time cannot be null");
    }

}
