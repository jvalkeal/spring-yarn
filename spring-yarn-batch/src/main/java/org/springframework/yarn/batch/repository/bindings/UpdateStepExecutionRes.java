package org.springframework.yarn.batch.repository.bindings;

public class UpdateStepExecutionRes extends BaseObject {

    public String message;
    public String status;
    
    public UpdateStepExecutionRes() {
        super("UpdateStepExecutionRes");
    }

}
