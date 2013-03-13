package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class FindRunningJobExecutionsReq extends BaseObject {

    public String jobName;
    
    public FindRunningJobExecutionsReq() {
        super("FindRunningJobExecutionsReq");
    }


}
