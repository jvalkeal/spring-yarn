package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;


public class GetStepExecutionReq extends BaseObject {

    public JobExecutionType jobExecution;
    public Long stepExecutionId;
    
    public GetStepExecutionReq() {
        super("GetStepExecutionReq");
    }

}
