package org.springframework.yarn.batch.repository.bindings;

import java.util.List;

import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for getting a job instance.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobInstanceDao#getJobInstances(String,int,int)
 * @see org.springframework.batch.core.JobInstance
 * 
 */
public class GetJobInstancesRes extends BaseResponseObject {

    public List<JobInstanceType> jobInstances;
    
    public GetJobInstancesRes() {
        super("GetJobInstancesRes");
    }

    public GetJobInstancesRes(List<JobInstanceType> jobInstances) {
        this();
        this.jobInstances = jobInstances;
    }

    public List<JobInstanceType> getJobInstances() {
        return jobInstances;
    }

}
