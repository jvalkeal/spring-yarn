package org.springframework.yarn.batch.repository;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.batch.repository.bindings.GetExecutionContextReq;
import org.springframework.yarn.batch.repository.bindings.GetExecutionContextRes;
import org.springframework.yarn.batch.repository.bindings.SaveExecutionContextReq;
import org.springframework.yarn.batch.repository.bindings.SaveExecutionContextRes;
import org.springframework.yarn.batch.repository.bindings.UpdateExecutionContextReq;
import org.springframework.yarn.batch.repository.bindings.UpdateExecutionContextRes;
import org.springframework.yarn.integration.ip.mind.AppmasterMindScOperations;

/**
 * Proxy implementation of {@link ExecutionContextDao}. Passes dao
 * methods to a remote repository via service calls using 
 * {@link RpcMessage} messages.
 * 
 * @author Janne Valkealahti
 *
 */
public class RemoteExecutionContextDao extends AbstractRemoteDao implements ExecutionContextDao {

    public RemoteExecutionContextDao() {
        super();
    }

    public RemoteExecutionContextDao(AppmasterMindScOperations appmasterScOperations) {
        super(appmasterScOperations);
    }
    
    @Override
    public ExecutionContext getExecutionContext(JobExecution jobExecution) {
        ExecutionContext executionContext = null;
        try {
            GetExecutionContextReq request = JobRepositoryRpcFactory.buildGetExecutionContextReq(jobExecution);
            GetExecutionContextRes response = (GetExecutionContextRes) getAppmasterScOperations().doMindRequest(request);
            executionContext = JobRepositoryRpcFactory.convertExecutionContextType(response.executionContext);
        } catch (Exception e) {
            throw convertException(e);
        }                
        return executionContext;
    }

    @Override
    public ExecutionContext getExecutionContext(StepExecution stepExecution) {
        ExecutionContext executionContext = null;
        try {
            GetExecutionContextReq request = JobRepositoryRpcFactory.buildGetExecutionContextReq(stepExecution);
            GetExecutionContextRes response = (GetExecutionContextRes) getAppmasterScOperations().doMindRequest(request);
            executionContext = JobRepositoryRpcFactory.convertExecutionContextType(response.executionContext);            
        } catch (Exception e) {
            throw convertException(e);
        }                
        return executionContext;
    }

    @Override
    public void saveExecutionContext(JobExecution jobExecution) {
        try {
            SaveExecutionContextReq request = JobRepositoryRpcFactory.buildSaveExecutionContextReq(jobExecution);
            SaveExecutionContextRes response = (SaveExecutionContextRes) getAppmasterScOperations().doMindRequest(request);
            checkResponseMayThrow(response);
        } catch (Exception e) {
            throw convertException(e);
        }
    }

    @Override
    public void saveExecutionContext(StepExecution stepExecution) {
        try {
            SaveExecutionContextReq request = JobRepositoryRpcFactory.buildSaveExecutionContextReq(stepExecution);
            SaveExecutionContextRes response = (SaveExecutionContextRes) getAppmasterScOperations().doMindRequest(request);
            checkResponseMayThrow(response);
        } catch (Exception e) {
            throw convertException(e);
        }
    }

    @Override
    public void updateExecutionContext(JobExecution jobExecution) {
        try {
            UpdateExecutionContextReq request = JobRepositoryRpcFactory.buildUpdateExecutionContextReq(jobExecution);
            UpdateExecutionContextRes response = (UpdateExecutionContextRes) getAppmasterScOperations().doMindRequest(request);
            checkResponseMayThrow(response);
        } catch (Exception e) {
            throw convertException(e);
        }
    }

    @Override
    public void updateExecutionContext(StepExecution stepExecution) {
        try {
            UpdateExecutionContextReq request = JobRepositoryRpcFactory.buildUpdateExecutionContextReq(stepExecution);
            UpdateExecutionContextRes response = (UpdateExecutionContextRes) getAppmasterScOperations().doMindRequest(request);
            checkResponseMayThrow(response);
        } catch (Exception e) {
            throw convertException(e);
        }
    }

}
