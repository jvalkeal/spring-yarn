package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for adding step executions.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteStepExecutionDao#addStepExecutions(JobExecution)
 * @see org.springframework.batch.core.JobExecution
 * 
 */
public class AddStepExecutionsRes extends BaseResponseObject {

    public JobExecutionType jobExecution;
    
    public AddStepExecutionsRes() {
        super("AddStepExecutionsRes");
    }

}
