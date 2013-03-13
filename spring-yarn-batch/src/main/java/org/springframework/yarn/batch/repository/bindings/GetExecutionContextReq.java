package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;


public class GetExecutionContextReq extends BaseObject {

    public JobExecutionType jobExecution;
    public StepExecutionType stepExecution;
    
    public GetExecutionContextReq() {
        super("GetExecutionContextReq");
    }

}
