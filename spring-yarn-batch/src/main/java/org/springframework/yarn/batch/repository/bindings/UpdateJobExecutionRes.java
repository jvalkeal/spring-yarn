package org.springframework.yarn.batch.repository.bindings;

public class UpdateJobExecutionRes extends BaseObject {

    public String message;
    public String status;
    
    public UpdateJobExecutionRes() {
        super("UpdateJobExecutionRes");
    }

}
