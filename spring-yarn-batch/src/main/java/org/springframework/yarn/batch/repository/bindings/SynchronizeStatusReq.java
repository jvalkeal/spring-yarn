package org.springframework.yarn.batch.repository.bindings;

public class SynchronizeStatusReq extends BaseObject {

    public JobExecutionType jobExecution;
    
    public SynchronizeStatusReq() {
        super("SynchronizeStatusReq");
    }


}
