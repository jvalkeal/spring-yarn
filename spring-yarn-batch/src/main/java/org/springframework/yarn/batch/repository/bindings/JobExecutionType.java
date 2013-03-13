package org.springframework.yarn.batch.repository.bindings;

import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

/**
 * Bindings for {@link org.springframework.batch.core.JobExecution}.
 * 
 * @author Janne Valkealahti
 *
 */
public class JobExecutionType extends BaseObject {

    public Long id;
    public Integer version;
    public JobInstanceType jobInstance;
    public List<StepExecutionType> stepExecutions;
    public BatchStatus status;
    public Long startTime;
    public Long createTime;
    public Long endTime;
    public Long lastUpdated;
    public String exitStatus;
    public ExecutionContextType executionContext;
    
    public JobExecutionType() {
        super("JobExecutionType");
    }

}
