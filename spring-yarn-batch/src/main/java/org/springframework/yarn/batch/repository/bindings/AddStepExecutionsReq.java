package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class AddStepExecutionsReq extends BaseObject {

    public JobExecutionType jobExecution;
    
    public AddStepExecutionsReq() {
        super("AddStepExecutionsReq");
    }

}
