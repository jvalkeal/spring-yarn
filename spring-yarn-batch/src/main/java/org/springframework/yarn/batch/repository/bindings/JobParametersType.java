package org.springframework.yarn.batch.repository.bindings;

import java.util.Map;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class JobParametersType extends BaseObject {

    public Map<String,JobParameterType> parameters;
    
    public JobParametersType() {
        super("JobParametersType");
    }

}
