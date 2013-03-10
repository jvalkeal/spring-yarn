package org.springframework.yarn.batch.repository;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.batch.repository.bindings.GetExecutionContextRes;
import org.springframework.yarn.batch.repository.bindings.SaveExecutionContextRes;
import org.springframework.yarn.batch.repository.bindings.UpdateExecutionContextRes;
import org.springframework.yarn.client.AppmasterScOperations;
import org.springframework.yarn.integration.ip.mind.MindRpcMessageHolder;

public class RemoteExecutionContextDao extends AbstractRemoteDao implements ExecutionContextDao {

    public RemoteExecutionContextDao() {
        super();
    }

    public RemoteExecutionContextDao(AppmasterScOperations appmasterScOperations) {
        super(appmasterScOperations);
    }
    
    @Override
    public ExecutionContext getExecutionContext(JobExecution jobExecution) {
        ExecutionContext executionContext = null;
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildGetExecutionContextReq(jobExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();

            GetExecutionContextRes responseBody = JobRepositoryRpcFactory.convert(holder, GetExecutionContextRes.class);
            executionContext = JobRepositoryRpcFactory.convertExecutionContextType(responseBody.executionContext);
            
        } catch (Exception e) {
            throw convertException(e);
        }                
        return executionContext;
    }

    @Override
    public ExecutionContext getExecutionContext(StepExecution stepExecution) {
        ExecutionContext executionContext = null;
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildGetExecutionContextReq(stepExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();

            GetExecutionContextRes responseBody = JobRepositoryRpcFactory.convert(holder, GetExecutionContextRes.class);
            executionContext = JobRepositoryRpcFactory.convertExecutionContextType(responseBody.executionContext);            
        } catch (Exception e) {
            throw convertException(e);
        }                
        return executionContext;
    }

    @Override
    public void saveExecutionContext(JobExecution jobExecution) {
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildSaveExecutionContextReq(jobExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);            
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();            
            SaveExecutionContextRes responseBody = JobRepositoryRpcFactory.convert(holder, SaveExecutionContextRes.class);
            checkResponseMayThrow(responseBody);
        } catch (Exception e) {
            throw convertException(e);
        }
    }

    @Override
    public void saveExecutionContext(StepExecution stepExecution) {
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildSaveExecutionContextReq(stepExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);            
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();            
            SaveExecutionContextRes responseBody = JobRepositoryRpcFactory.convert(holder, SaveExecutionContextRes.class);
            checkResponseMayThrow(responseBody);
        } catch (Exception e) {
            throw convertException(e);
        }
    }

    @Override
    public void updateExecutionContext(JobExecution jobExecution) {
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildUpdateExecutionContextReq(jobExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);            
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();            
            UpdateExecutionContextRes responseBody = JobRepositoryRpcFactory.convert(holder, UpdateExecutionContextRes.class);
            checkResponseMayThrow(responseBody);
        } catch (Exception e) {
            throw convertException(e);
        }
    }

    @Override
    public void updateExecutionContext(StepExecution stepExecution) {
        try {
            RpcMessage<?> request = JobRepositoryRpcFactory.buildUpdateExecutionContextReq(stepExecution);
            RpcMessage<?> response = getAppmasterScOperations().get(request);            
            MindRpcMessageHolder holder = (MindRpcMessageHolder) response.getBody();            
            UpdateExecutionContextRes responseBody = JobRepositoryRpcFactory.convert(holder, UpdateExecutionContextRes.class);
            checkResponseMayThrow(responseBody);
        } catch (Exception e) {
            throw convertException(e);
        }
    }

}
