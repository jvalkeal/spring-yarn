package org.springframework.yarn.batch.repository.bindings;

public class SaveJobExecutionReq extends BaseObject {

    public JobExecutionType jobExecution;
    
    public SaveJobExecutionReq() {
        super("SaveJobExecutionReq");
    }

}
