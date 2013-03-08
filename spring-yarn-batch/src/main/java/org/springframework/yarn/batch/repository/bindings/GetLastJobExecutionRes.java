package org.springframework.yarn.batch.repository.bindings;

public class GetLastJobExecutionRes extends BaseObject {

    public String message;
    public String status;
    public JobExecutionType jobExecution;
    
    public GetLastJobExecutionRes() {
        super("GetLastJobExecutionRes");
    }

}
