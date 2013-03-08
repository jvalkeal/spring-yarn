package org.springframework.yarn.batch.repository.bindings;

public class SaveExecutionContextReq extends BaseObject {

    public JobExecutionType jobExecution;
    public StepExecutionType stepExecution;
    
    public SaveExecutionContextReq() {
        super("SaveExecutionContextReq");
    }

}
