package org.springframework.yarn.batch.repository.bindings;

public class AddStepExecutionsReq extends BaseObject {

    public JobExecutionType jobExecution;
    
    public AddStepExecutionsReq() {
        super("AddStepExecutionsReq");
    }

}
