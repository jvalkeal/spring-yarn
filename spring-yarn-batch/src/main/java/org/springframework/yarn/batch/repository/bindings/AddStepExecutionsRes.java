package org.springframework.yarn.batch.repository.bindings;

public class AddStepExecutionsRes extends BaseObject {

    public String message;
    public String status;
    
    public JobExecutionType jobExecution;
    
    public AddStepExecutionsRes() {
        super("AddStepExecutionsRes");
    }

}
