package org.springframework.yarn.batch.repository.bindings;

public class UpdateStepExecutionReq extends BaseObject {

    public StepExecutionType stepExecution;
    
    public UpdateStepExecutionReq() {
        super("UpdateStepExecutionReq");
    }

}
