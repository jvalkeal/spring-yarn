package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class GetLastJobExecutionRes extends BaseObject {

    public String message;
    public String status;
    public JobExecutionType jobExecution;
    
    public GetLastJobExecutionRes() {
        super("GetLastJobExecutionRes");
    }

}
