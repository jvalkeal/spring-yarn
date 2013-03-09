package org.springframework.yarn.batch.repository.bindings;

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
