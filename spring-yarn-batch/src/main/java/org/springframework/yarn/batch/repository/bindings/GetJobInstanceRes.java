package org.springframework.yarn.batch.repository.bindings;

import org.springframework.batch.core.JobParameters;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for getting a job instance.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobInstanceDao#getJobInstance(String,JobParameters)
 * @see org.springframework.batch.core.JobInstance
 * 
 */
public class GetJobInstanceRes extends BaseResponseObject {

    public JobInstanceType jobInstance;
    
    public GetJobInstanceRes() {
        super("GetJobInstanceRes");
    }

    public GetJobInstanceRes(JobInstanceType jobInstance) {
        this();
        this.jobInstance = jobInstance;
    }

    public JobInstanceType getJobInstance() {
        return jobInstance;
    }

}
