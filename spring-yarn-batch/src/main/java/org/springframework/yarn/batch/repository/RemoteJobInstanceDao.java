package org.springframework.yarn.batch.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.util.Assert;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.batch.repository.bindings.CreateJobInstanceReq;
import org.springframework.yarn.batch.repository.bindings.CreateJobInstanceRes;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceByIdReq;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceByIdRes;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceReq;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceRes;
import org.springframework.yarn.batch.repository.bindings.GetJobInstancesReq;
import org.springframework.yarn.batch.repository.bindings.GetJobInstancesRes;
import org.springframework.yarn.batch.repository.bindings.GetJobNamesReq;
import org.springframework.yarn.batch.repository.bindings.GetJobNamesRes;
import org.springframework.yarn.batch.repository.bindings.JobInstanceType;
import org.springframework.yarn.integration.ip.mind.AppmasterMindScOperations;

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

    public RemoteJobInstanceDao(AppmasterMindScOperations appmasterScOperations) {
        super(appmasterScOperations);
    }

    @Override
    public JobInstance createJobInstance(String jobName, JobParameters jobParameters) {
        Assert.notNull(jobName, "Job name must not be null.");
        Assert.notNull(jobParameters, "JobParameters must not be null.");
        
        JobInstance jobInstance = null;
        try {
            CreateJobInstanceReq request = JobRepositoryRpcFactory.buildCreateJobInstanceReq(jobName, jobParameters);
            CreateJobInstanceRes response = (CreateJobInstanceRes) getAppmasterScOperations().doMindRequest(request);
            jobInstance = JobRepositoryRpcFactory.convertJobInstanceType(response.getJobInstance());
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
            GetJobInstanceReq request = JobRepositoryRpcFactory.buildGetJobInstanceReq(jobName, jobParameters);        
            GetJobInstanceRes response = (GetJobInstanceRes) getAppmasterScOperations().doMindRequest(request);            
            if(response.jobInstance != null) {
                jobInstance = JobRepositoryRpcFactory.convertJobInstanceType(response.jobInstance);
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
            GetJobInstanceByIdReq request = JobRepositoryRpcFactory.buildGetJobInstanceByIdReq(instanceId);
            GetJobInstanceByIdRes response = (GetJobInstanceByIdRes) getAppmasterScOperations().doMindRequest(request);            
            if(response.jobInstance != null) {
                jobInstance = JobRepositoryRpcFactory.convertJobInstanceType(response.jobInstance);
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
            GetJobInstancesReq request = JobRepositoryRpcFactory.buildGetJobInstancesReq(jobName, start, count);
            GetJobInstancesRes response = (GetJobInstancesRes) getAppmasterScOperations().doMindRequest(request);                        
            for(JobInstanceType jobInstanceType : response.getJobInstances()) {
                jobInstances.add(JobRepositoryRpcFactory.convertJobInstanceType(jobInstanceType));
            }
        } catch (Exception e) {
            throw convertException(e);
        }        
        return jobInstances;
    }

    @Override
    public List<String> getJobNames() {
        List<String> jobNames = null;
        try {
            GetJobNamesReq request = JobRepositoryRpcFactory.buildGetJobNamesReq();
            GetJobNamesRes response = (GetJobNamesRes) getAppmasterScOperations().doMindRequest(request);
            jobNames = response.getJobNames();
        } catch (Exception e) {
            throw convertException(e);
        }
        return jobNames;
    }

}
