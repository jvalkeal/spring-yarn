package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class JobInstanceType extends BaseObject {

    public Long id;
    public Integer version;
    public String jobName;
    public JobParametersType jobParameters;
    
    public JobInstanceType() {
        super("JobInstanceType");
    }

}
