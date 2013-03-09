package org.springframework.yarn.batch.repository.bindings;

/**
 * Response for updating job execution.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobExecutionDao#updateJobExecution(JobExecution)
 * @see org.springframework.batch.core.JobExecution
 * 
 */
public class UpdateJobExecutionRes extends BaseResponseObject {

    public Integer version;
    
    public UpdateJobExecutionRes() {
        super("UpdateJobExecutionRes");
    }
    
    public UpdateJobExecutionRes(Integer version) {
        this();
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }
    
}
