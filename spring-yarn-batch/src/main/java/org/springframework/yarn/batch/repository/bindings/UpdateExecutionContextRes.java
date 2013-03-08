package org.springframework.yarn.batch.repository.bindings;

public class UpdateExecutionContextRes extends BaseObject {

    public String message;
    public String status;
    
    public UpdateExecutionContextRes() {
        super("UpdateExecutionContextRes");
    }

}
