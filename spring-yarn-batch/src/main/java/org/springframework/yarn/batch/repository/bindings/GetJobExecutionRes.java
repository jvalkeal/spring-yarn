package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for getting a job execution.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobExecutionDao#getJobExecution(Long)
 * @see org.springframework.batch.core.JobExecution
 * 
 */
public class GetJobExecutionRes extends BaseResponseObject {

    public JobExecutionType jobExecution;
    
    public GetJobExecutionRes() {
        super("GetJobExecutionRes");
    }

}
