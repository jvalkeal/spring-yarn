package org.springframework.yarn.batch.repository.bindings;

public class GetJobExecutionReq extends BaseObject {

    public Long executionId;
    
    public GetJobExecutionReq() {
        super("GetJobExecutionReq");
    }


}
