package org.springframework.yarn.batch.repository.bindings;

import java.util.Map;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class CreateJobInstanceReq extends BaseObject {

    public String jobName;
    public Map<String, JobParameterType> jobParameters;
    
    public CreateJobInstanceReq() {
        super("CreateJobInstanceReq");
    }

}
