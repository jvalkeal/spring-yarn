package org.springframework.yarn.batch.repository.bindings;

public class GetStepExecutionRes extends BaseObject {

    public String message;
    public String status;
    public StepExecutionType stepExecution;
    
    public GetStepExecutionRes() {
        super("GetStepExecutionRes");
    }

}
