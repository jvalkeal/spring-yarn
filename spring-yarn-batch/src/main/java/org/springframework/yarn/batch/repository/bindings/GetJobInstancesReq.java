package org.springframework.yarn.batch.repository.bindings;

public class GetJobInstancesReq extends BaseObject {

    public String jobName;
    public Integer start;
    public Integer count;
    
    public GetJobInstancesReq() {
        super("GetJobInstancesReq");
    }

}
