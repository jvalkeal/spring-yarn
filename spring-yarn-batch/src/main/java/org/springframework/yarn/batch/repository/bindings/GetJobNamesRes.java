package org.springframework.yarn.batch.repository.bindings;

import java.util.List;

import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for getting a job instance.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobInstanceDao#getJobNames()
 * 
 */
public class GetJobNamesRes extends BaseResponseObject {

    public List<String> getJobNames;
    
    public GetJobNamesRes() {
        super("GetJobNamesRes");
    }

    public GetJobNamesRes(List<String> getJobNames) {
        this();
        this.getJobNames = getJobNames;
    }

    public List<String> getJobNames() {
        return getJobNames;
    }

}
