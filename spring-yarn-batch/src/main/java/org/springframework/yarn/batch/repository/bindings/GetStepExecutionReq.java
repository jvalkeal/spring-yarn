package org.springframework.yarn.batch.repository.bindings;


public class GetStepExecutionReq extends BaseObject {

    public JobExecutionType jobExecution;
    public Long stepExecutionId;
    
    public GetStepExecutionReq() {
        super("GetStepExecutionReq");
    }

}
