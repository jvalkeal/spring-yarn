package org.springframework.yarn.batch.repository.bindings;

import org.springframework.batch.core.BatchStatus;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Response for synching status.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteJobExecutionDao#synchronizeStatus(JobExecution)
 * @see org.springframework.batch.core.JobExecution
 * 
 */
public class SynchronizeStatusRes extends BaseResponseObject {

    public Integer version;
    public BatchStatus status;
    
    public SynchronizeStatusRes() {
        super("SynchronizeStatusRes");
    }
    
    public SynchronizeStatusRes(Integer version, BatchStatus status) {
        this();
        this.version = version;
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public BatchStatus getStatus() {
        return status;
    }

}
