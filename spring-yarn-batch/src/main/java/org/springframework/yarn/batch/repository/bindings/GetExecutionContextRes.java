package org.springframework.yarn.batch.repository.bindings;

public class GetExecutionContextRes extends BaseObject {

    public String message;
    public String status;
    public ExecutionContextType executionContext;
    
    public GetExecutionContextRes() {
        super("GetExecutionContextRes");
    }

}
