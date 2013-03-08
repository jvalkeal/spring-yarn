package org.springframework.yarn.batch.repository.bindings;

import java.util.Map;

public class JobParametersType extends BaseObject {

    public Map<String,JobParameterType> parameters;
    
    public JobParametersType() {
        super("JobParametersType");
    }

}
