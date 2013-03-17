package org.springframework.yarn.batch.repository.bindings;

import java.util.Set;

import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for finding a running job executions.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobExecutionDao#findRunningJobExecutions(String)
 * @see org.springframework.batch.core.JobExecution
 * 
 */
public class FindRunningJobExecutionsRes extends BaseResponseObject {

    public Set<JobExecutionType> jobExecutions;
    
    public FindRunningJobExecutionsRes() {
        super("FindRunningJobExecutionsRes");
    }

}
