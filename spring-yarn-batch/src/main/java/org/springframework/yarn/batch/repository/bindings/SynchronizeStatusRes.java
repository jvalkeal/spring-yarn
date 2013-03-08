package org.springframework.yarn.batch.repository.bindings;

import org.springframework.batch.core.BatchStatus;

public class SynchronizeStatusRes extends BaseObject {

    public String message;
    public String xstatus;
    
    public BatchStatus status;
    public Integer version;
    
    public SynchronizeStatusRes() {
        super("SynchronizeStatusRes");
    }

}
