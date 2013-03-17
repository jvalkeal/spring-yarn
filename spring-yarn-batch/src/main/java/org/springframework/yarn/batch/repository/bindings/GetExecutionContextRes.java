package org.springframework.yarn.batch.repository.bindings;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for getting execution context.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteExecutionContextDao#getExecutionContext(JobExecution)
 * @see org.springframework.yarn.batch.repository.RemoteExecutionContextDao#getExecutionContext(StepExecution)
 * @see org.springframework.batch.core.JobInstance
 * 
 */
public class GetExecutionContextRes extends BaseResponseObject {

    public ExecutionContextType executionContext;
    
    public GetExecutionContextRes() {
        super("GetExecutionContextRes");
    }

}
