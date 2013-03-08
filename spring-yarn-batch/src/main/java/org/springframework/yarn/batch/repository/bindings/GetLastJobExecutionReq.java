package org.springframework.yarn.batch.repository.bindings;

public class GetLastJobExecutionReq extends BaseObject {

    public JobInstanceType jobInstance;

    public GetLastJobExecutionReq() {
        super("GetLastJobExecutionReq");
    }

}
