package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for getting a job instance by id.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobInstanceDao#getJobInstance(Long)
 * @see org.springframework.batch.core.JobInstance
 * 
 */
public class GetJobInstanceByIdRes extends BaseResponseObject {

    public JobInstanceType jobInstance;
    
    public GetJobInstanceByIdRes() {
        super("GetJobInstanceByIdRes");
    }
    
    public GetJobInstanceByIdRes(JobInstanceType jobInstance) {
        this();
        this.jobInstance = jobInstance;
    }

    public JobInstanceType getJobInstance() {
        return jobInstance;
    }

}
