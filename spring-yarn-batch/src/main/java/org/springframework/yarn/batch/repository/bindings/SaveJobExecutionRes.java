package org.springframework.yarn.batch.repository.bindings;

import org.springframework.batch.core.JobExecution;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for saving a job execution.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobExecutionDao#saveJobExecution(JobExecution)
 * @see org.springframework.batch.core.JobExecution
 * 
 */
public class SaveJobExecutionRes extends BaseResponseObject {

    public Long id;
    public Integer version;
    
    public SaveJobExecutionRes() {
        super("SaveJobExecutionRes");
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

}
