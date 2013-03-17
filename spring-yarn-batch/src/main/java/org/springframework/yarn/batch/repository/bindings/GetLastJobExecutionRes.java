package org.springframework.yarn.batch.repository.bindings;

import org.springframework.batch.core.JobInstance;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for getting a last job execution.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobExecutionDao#getLastJobExecution(JobInstance)
 * @see org.springframework.batch.core.JobInstance
 * @see org.springframework.batch.core.JobExecution
 * 
 */
public class GetLastJobExecutionRes extends BaseResponseObject {

    public JobExecutionType jobExecution;
    
    public GetLastJobExecutionRes() {
        super("GetLastJobExecutionRes");
    }

}
