package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class UpdateStepExecutionReq extends BaseObject {

    public StepExecutionType stepExecution;
    
    public UpdateStepExecutionReq() {
        super("UpdateStepExecutionReq");
    }

}
