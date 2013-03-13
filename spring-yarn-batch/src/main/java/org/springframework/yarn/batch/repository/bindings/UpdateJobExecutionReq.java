package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class UpdateJobExecutionReq extends BaseObject {

    public JobExecutionType jobExecution;
    
    public UpdateJobExecutionReq() {
        super("UpdateJobExecutionReq");
    }


}
