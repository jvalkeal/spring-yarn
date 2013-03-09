package org.springframework.yarn.batch.repository.bindings;

/**
 * Response for saving execution context.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteExecutionContextDao#saveExecutionContext(JobExecution)
 * @see org.springframework.yarn.batch.repository.RemoteExecutionContextDao#saveExecutionContext(StepExecution)
 * @see org.springframework.batch.core.JobExecution
 * @see org.springframework.batch.core.StepExecution
 * 
 */
public class SaveExecutionContextRes extends BaseResponseObject {

    public SaveExecutionContextRes() {
        super("SaveExecutionContextRes");
    }

}
