package org.springframework.yarn.batch.repository.bindings;

public class SaveJobExecutionRes extends BaseObject {

    public String message;
    public String status;
    public Long id;
    public Integer version;
    
    public SaveJobExecutionRes() {
        super("SaveJobExecutionRes");
    }

}
