package org.springframework.yarn.batch.repository.bindings;


public class GetExecutionContextReq extends BaseObject {

    public JobExecutionType jobExecution;
    public StepExecutionType stepExecution;
    
    public GetExecutionContextReq() {
        super("GetExecutionContextReq");
    }

}
