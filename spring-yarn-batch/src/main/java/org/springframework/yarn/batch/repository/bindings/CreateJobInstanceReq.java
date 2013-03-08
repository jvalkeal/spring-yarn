package org.springframework.yarn.batch.repository.bindings;

import java.util.Map;

public class CreateJobInstanceReq extends BaseObject {

    public String jobName;
    public Map<String, JobParameterType> jobParameters;
    
    public CreateJobInstanceReq() {
        super("CreateJobInstanceReq");
    }

}
