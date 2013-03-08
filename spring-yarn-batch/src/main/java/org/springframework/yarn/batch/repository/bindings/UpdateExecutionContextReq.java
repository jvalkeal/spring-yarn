package org.springframework.yarn.batch.repository.bindings;

public class UpdateExecutionContextReq extends BaseObject {

    public JobExecutionType jobExecution;
    public StepExecutionType stepExecution;
    
    public UpdateExecutionContextReq() {
        super("UpdateExecutionContextReq");
    }

}
