package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class SaveStepExecutionReq extends BaseObject {

    public StepExecutionType stepExecution;
    
    public SaveStepExecutionReq() {
        super("SaveStepExecutionReq");
    }

}
