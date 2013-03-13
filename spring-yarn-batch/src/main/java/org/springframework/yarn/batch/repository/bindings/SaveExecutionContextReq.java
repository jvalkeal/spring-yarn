package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class SaveExecutionContextReq extends BaseObject {

    public JobExecutionType jobExecution;
    public StepExecutionType stepExecution;
    
    public SaveExecutionContextReq() {
        super("SaveExecutionContextReq");
    }

}
