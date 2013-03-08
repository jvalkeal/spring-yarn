package org.springframework.yarn.batch.repository;

import java.util.ArrayList;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.util.Assert;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.batch.repository.bindings.AddStepExecutionsRes;
import org.springframework.yarn.batch.repository.bindings.GetStepExecutionRes;
import org.springframework.yarn.batch.repository.bindings.SaveStepExecutionRes;
import org.springframework.yarn.batch.repository.bindings.UpdateStepExecutionRes;
import org.springframework.yarn.client.AppmasterScOperations;
import org.springframework.yarn.integration.ip.mind.MindRpcMessageHolder;

public class RemoteStepExecutionDao extends AbstractRemoteDao implements StepExecutionDao {

    public RemoteStepExecutionDao() {
        super();
    }

    public RemoteStepExecutionDao(AppmasterScOperations appmasterScOperations) {
        super(appmasterScOperations);
    }
    
    @Override
    public void saveStepExecution(StepExecution stepExecution) {
        Assert.isNull(stepExecution.getId(), "StepExecution can't already have an id assigned");
        Assert.isNull(stepExecution.getVersion(), "StepExecution can't already have a version assigned");

        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildSaveStepExecutionReq(stepExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            SaveStepExecutionRes responseBody = JobRepositoryRpcFactory.convert(holder, SaveStepExecutionRes.class);
            // should we get version from a response?
            stepExecution.setId(responseBody.id);
            stepExecution.incrementVersion();
        } catch (Exception e) {
            throw convertException(e);
        }
    }

    @Override
    public void updateStepExecution(StepExecution stepExecution) {
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildUpdateStepExecutionReq(stepExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            UpdateStepExecutionRes responseBody = JobRepositoryRpcFactory.convert(holder, UpdateStepExecutionRes.class);
            // should we get version from a response?
            stepExecution.incrementVersion();
        } catch (Exception e) {
            throw convertException(e);
        }        
    }

    @Override
    public StepExecution getStepExecution(JobExecution jobExecution, Long stepExecutionId) {
        StepExecution stepExecution = null;
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildGetStepExecutionReq(jobExecution, stepExecutionId);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            GetStepExecutionRes responseBody = JobRepositoryRpcFactory.convert(holder, GetStepExecutionRes.class);
            if(responseBody.stepExecution != null) {
                stepExecution = JobRepositoryRpcFactory.convertStepExecutionType(responseBody.stepExecution);                
            }
        } catch (Exception e) {
            throw convertException(e);
        }
        return stepExecution;
    }

    @Override
    public void addStepExecutions(JobExecution jobExecution) {
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildAddStepExecutionReq(jobExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();
            AddStepExecutionsRes responseBody = JobRepositoryRpcFactory.convert(holder, AddStepExecutionsRes.class);
            JobExecution convertJobExecutionType = JobRepositoryRpcFactory.convertJobExecutionType(responseBody.jobExecution);            
            jobExecution.addStepExecutions(new ArrayList(convertJobExecutionType.getStepExecutions()));            
        } catch (Exception e) {
            throw convertException(e);
        }        
    }

}
