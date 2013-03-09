package org.springframework.yarn.batch.repository.bindings;

import org.springframework.batch.core.StepExecution;

/**
 * Response for saving step execution.
 * 
 * @author Janne Valkealahti
 *
 * @see org.springframework.yarn.batch.repository.RemoteStepExecutionDao#updateStepExecution(StepExecution)
 * @see org.springframework.batch.core.StepExecution
 * 
 */
public class UpdateStepExecutionRes extends BaseResponseObject {

    public Long id;
    public Integer version;
    
    public UpdateStepExecutionRes() {
        super("UpdateStepExecutionRes");
    }

    public UpdateStepExecutionRes(Long id, Integer version) {
        this();
        this.id = id;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }
    
}
