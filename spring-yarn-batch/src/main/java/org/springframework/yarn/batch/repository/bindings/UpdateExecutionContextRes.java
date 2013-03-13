package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for saving execution context.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteExecutionContextDao#updateExecutionContext(JobExecution)
 * @see org.springframework.yarn.batch.repository.RemoteExecutionContextDao#updateExecutionContext(StepExecution)
 * @see org.springframework.batch.core.JobExecution
 * @see org.springframework.batch.core.StepExecution
 * 
 */
public class UpdateExecutionContextRes extends BaseResponseObject {

    public String message;
    public String status;
    
    public UpdateExecutionContextRes() {
        super("UpdateExecutionContextRes");
    }

}
