package org.springframework.yarn.batch.repository.bindings;

import org.springframework.batch.core.JobParameter.ParameterType;

public class JobParameterType extends BaseObject {

    public Object parameter;
    
    public ParameterType parameterType;
    
    public JobParameterType() {
        super("JobParameter");
    }

}
