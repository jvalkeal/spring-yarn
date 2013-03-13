package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class GetStepExecutionRes extends BaseObject {

    public String message;
    public String status;
    public StepExecutionType stepExecution;
    
    public GetStepExecutionRes() {
        super("GetStepExecutionRes");
    }

}
