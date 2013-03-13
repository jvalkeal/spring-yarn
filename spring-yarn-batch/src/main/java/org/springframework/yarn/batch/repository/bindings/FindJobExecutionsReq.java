package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;


public class FindJobExecutionsReq extends BaseObject {

    public JobInstanceType jobInstance;
    
    public FindJobExecutionsReq() {
        super("FindJobExecutionsReq");
    }

}
