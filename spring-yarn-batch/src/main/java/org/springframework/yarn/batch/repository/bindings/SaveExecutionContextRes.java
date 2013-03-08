package org.springframework.yarn.batch.repository.bindings;

public class SaveExecutionContextRes extends BaseObject {

    public String message;
    public String status;
    
    public SaveExecutionContextRes() {
        super("SaveExecutionContextRes");
    }

}
