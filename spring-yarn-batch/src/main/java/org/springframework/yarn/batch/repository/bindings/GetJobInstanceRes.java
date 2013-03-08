package org.springframework.yarn.batch.repository.bindings;

public class GetJobInstanceRes extends BaseObject {

    public String message;
    public String status;
    public JobInstanceType jobInstance;
    
    public GetJobInstanceRes() {
        super("GetJobInstanceRes");
    }

}
