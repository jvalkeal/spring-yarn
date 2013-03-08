package org.springframework.yarn.batch.repository.bindings;

public class SaveStepExecutionReq extends BaseObject {

    public StepExecutionType stepExecution;
    
    public SaveStepExecutionReq() {
        super("SaveStepExecutionReq");
    }

}
