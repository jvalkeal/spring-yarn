package org.springframework.yarn.batch.repository;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.util.Assert;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceByIdRes;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceRes;
import org.springframework.yarn.batch.repository.bindings.JobInstanceType;
import org.springframework.yarn.client.AppmasterScOperations;
import org.springframework.yarn.integration.ip.mind.MindRpcMessageHolder;

/**
 * Proxy implementation of {@link JobInstanceDao}. Simply uses
 * {@link RpcMessage} instances to talk to remote service which
 * should handle the actual {@link JobInstanceDao} logic.
 * 
 * @author Janne Valkealahti
 *
 */
public class RemoteJobInstanceDao extends AbstractRemoteDao implements JobInstanceDao {

    public RemoteJobInstanceDao() {
        super();
    }

    public RemoteJobInstanceDao(AppmasterScOperations appmasterScOperations) {
        super(appmasterScOperations);
    }

    @Override
    public JobInstance createJobInstance(String jobName, JobParameters jobParameters) {
        Assert.notNull(jobName, "Job name must not be null.");
        Assert.notNull(jobParameters, "JobParameters must not be null.");
        
        JobInstance jobInstance = null;
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildCreateJobInstanceReq(jobName, jobParameters);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            JobInstanceType jobInstanceType = JobRepositoryRpcFactory.convert(holder, JobInstanceType.class);
            jobInstance = JobRepositoryRpcFactory.convertJobInstanceType(jobInstanceType);
        } catch (Exception e) {
            throw convertException(e);
        }        
        return jobInstance;
    }

    @Override
    public JobInstance getJobInstance(String jobName, JobParameters jobParameters) {
        Assert.notNull(jobName, "Job name must not be null.");
        Assert.notNull(jobParameters, "JobParameters must not be null.");
        
        JobInstance jobInstance = null;
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildGetJobInstanceReq(jobName, jobParameters);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            GetJobInstanceRes responseBody = JobRepositoryRpcFactory.convert(holder, GetJobInstanceRes.class);
            if(responseBody.jobInstance != null) {
                jobInstance = JobRepositoryRpcFactory.convertJobInstanceType(responseBody.jobInstance);                
            }
        } catch (Exception e) {
            throw convertException(e);
        }
        return jobInstance;
    }

    @Override
    public JobInstance getJobInstance(Long instanceId) {
        JobInstance jobInstance = null;
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildGetJobInstanceByIdReq(instanceId);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            GetJobInstanceByIdRes responseBody = JobRepositoryRpcFactory.convert(holder, GetJobInstanceByIdRes.class);
            if(responseBody.jobInstance != null) {
                jobInstance = JobRepositoryRpcFactory.convertJobInstanceType(responseBody.jobInstance);                
            }
        } catch (Exception e) {
            throw convertException(e);
        }        
        return jobInstance;
    }

    @Override
    public JobInstance getJobInstance(JobExecution jobExecution) {
        // TODO implement getJobInstance(JobExecution jobExecution)
        return null;
    }

    @Override
    public List<JobInstance> getJobInstances(String jobName, int start, int count) {
        List<JobInstance> jobInstances = new ArrayList<JobInstance>();
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildGetJobInstancesReq(jobName, start, count);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            List<JobInstanceType> jobInstanceTypes = JobRepositoryRpcFactory.convert(holder, new TypeReference<List<JobInstanceType>>(){});            
            for(JobInstanceType jobInstanceType : jobInstanceTypes) {
                jobInstances.add(JobRepositoryRpcFactory.convertJobInstanceType(jobInstanceType));                
            }
        } catch (Exception e) {
            throw convertException(e);
        }        
        return jobInstances;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getJobNames() {
        List<String> jobNames = null;
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildGetJobNamesReq();
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            jobNames = JobRepositoryRpcFactory.convert(holder, List.class);
        } catch (Exception e) {
            throw convertException(e);
        }
        return jobNames;
    }

}
