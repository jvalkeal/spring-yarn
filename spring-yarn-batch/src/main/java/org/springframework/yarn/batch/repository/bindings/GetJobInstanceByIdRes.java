package org.springframework.yarn.batch.repository.bindings;

public class GetJobInstanceByIdRes extends BaseObject {

    public String message;
    public String status;
    public JobInstanceType jobInstance;
    
    public GetJobInstanceByIdRes() {
        super("SaveStepExecutionRes");
    }

}
