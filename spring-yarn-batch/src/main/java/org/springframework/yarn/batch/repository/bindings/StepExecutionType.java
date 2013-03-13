package org.springframework.yarn.batch.repository.bindings;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class StepExecutionType extends BaseObject {

    public Long id;
    public Integer version;
    public JobExecutionType jobExecution;
    public String stepName;
    public BatchStatus status;
    public Integer readCount = 0;
    public Integer writeCount = 0;
    public Integer commitCount = 0;
    public Integer rollbackCount = 0;
    public Integer readSkipCount = 0;
    public Integer processSkipCount = 0;
    public Integer writeSkipCount = 0;
    public Long startTime;
    public Long endTime;
    public Long lastUpdated;
    public ExecutionContextType executionContext;
    public ExitStatus exitStatus;
    public Boolean terminateOnly;
    public Integer filterCount;

    public StepExecutionType() {
        super("StepExecutionType");
    }

}
