package org.springframework.yarn.batch.repository.bindings;

import org.springframework.batch.core.JobExecution;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for getting a last job execution.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteStepExecutionDao#getStepExecution(JobExecution,Long)
 * @see org.springframework.batch.core.JobExecution
 * @see org.springframework.batch.core.StepExecution
 * 
 */
public class GetStepExecutionRes extends BaseResponseObject {

    public StepExecutionType stepExecution;
    
    public GetStepExecutionRes() {
        super("GetStepExecutionRes");
    }

}
