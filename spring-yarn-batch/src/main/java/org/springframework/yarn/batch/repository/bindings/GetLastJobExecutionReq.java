package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class GetLastJobExecutionReq extends BaseObject {

    public JobInstanceType jobInstance;

    public GetLastJobExecutionReq() {
        super("GetLastJobExecutionReq");
    }

}
