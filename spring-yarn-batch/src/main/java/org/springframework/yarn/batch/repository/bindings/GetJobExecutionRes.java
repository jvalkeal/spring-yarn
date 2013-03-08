package org.springframework.yarn.batch.repository.bindings;


public class GetJobExecutionRes extends BaseObject {

    public String message;
    public String status;
    public JobExecutionType jobExecution;
    
    public GetJobExecutionRes() {
        super("GetJobExecutionRes");
    }

}
