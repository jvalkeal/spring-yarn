package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class SynchronizeStatusReq extends BaseObject {

    public JobExecutionType jobExecution;
    
    public SynchronizeStatusReq() {
        super("SynchronizeStatusReq");
    }


}
