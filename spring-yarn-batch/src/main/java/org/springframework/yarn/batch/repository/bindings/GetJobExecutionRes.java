package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;


public class GetJobExecutionRes extends BaseObject {

    public String message;
    public String status;
    public JobExecutionType jobExecution;
    
    public GetJobExecutionRes() {
        super("GetJobExecutionRes");
    }

}
