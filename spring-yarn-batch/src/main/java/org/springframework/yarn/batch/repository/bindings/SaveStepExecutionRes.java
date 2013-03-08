package org.springframework.yarn.batch.repository.bindings;

public class SaveStepExecutionRes extends BaseObject {

    public String message;
    public String status;
    public Long id;
    
    public SaveStepExecutionRes() {
        super("SaveStepExecutionRes");
    }

}
