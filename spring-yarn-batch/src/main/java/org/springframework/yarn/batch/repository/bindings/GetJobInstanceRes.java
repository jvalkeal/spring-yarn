package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class GetJobInstanceRes extends BaseObject {

    public String message;
    public String status;
    public JobInstanceType jobInstance;
    
    public GetJobInstanceRes() {
        super("GetJobInstanceRes");
    }

}
