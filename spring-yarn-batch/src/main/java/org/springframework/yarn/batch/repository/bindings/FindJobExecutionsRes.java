package org.springframework.yarn.batch.repository.bindings;

import java.util.List;

import org.springframework.batch.core.JobInstance;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for finding a job executions.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobExecutionDao#findJobExecutions(JobInstance)
 * @see org.springframework.batch.core.JobExecution
 * @see org.springframework.batch.core.JobInstance
 * 
 */
public class FindJobExecutionsRes extends BaseResponseObject {

    public List<JobExecutionType> jobExecutions;
    
    public FindJobExecutionsRes() {
        super("FindJobExecutionsRes");
    }

}
