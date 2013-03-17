package org.springframework.yarn.batch.repository.bindings;

import org.springframework.batch.core.JobParameters;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for creating a job instance.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobInstanceDao#createJobInstance(String,JobParameters)
 * @see org.springframework.batch.core.JobInstance
 * 
 */
public class CreateJobInstanceRes extends BaseResponseObject {

    public JobInstanceType jobInstance;
    
    public CreateJobInstanceRes() {
        super("CreateJobInstanceRes");
    }
    
    public CreateJobInstanceRes(JobInstanceType jobInstance) {
        this();
        this.jobInstance = jobInstance;
    }

    public JobInstanceType getJobInstance() {
        return jobInstance;
    }

}
