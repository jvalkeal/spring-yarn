package org.springframework.yarn.batch.repository.bindings;

public class UpdateJobExecutionReq extends BaseObject {

    public JobExecutionType jobExecution;
    
    public UpdateJobExecutionReq() {
        super("UpdateJobExecutionReq");
    }


}
