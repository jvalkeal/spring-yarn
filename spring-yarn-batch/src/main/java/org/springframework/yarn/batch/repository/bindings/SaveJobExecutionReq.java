package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class SaveJobExecutionReq extends BaseObject {

    public JobExecutionType jobExecution;
    
    public SaveJobExecutionReq() {
        super("SaveJobExecutionReq");
    }

}
