package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class SaveJobExecutionRes extends BaseObject {

    public String message;
    public String status;
    public Long id;
    public Integer version;
    
    public SaveJobExecutionRes() {
        super("SaveJobExecutionRes");
    }

}
