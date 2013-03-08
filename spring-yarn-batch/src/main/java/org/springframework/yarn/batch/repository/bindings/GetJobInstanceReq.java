package org.springframework.yarn.batch.repository.bindings;

import java.util.Map;

public class GetJobInstanceReq extends BaseObject {

    public String jobName;
    public Map<String, JobParameterType> jobParameters;
    
    public GetJobInstanceReq() {
        super("GetJobInstanceReq");
    }

}
