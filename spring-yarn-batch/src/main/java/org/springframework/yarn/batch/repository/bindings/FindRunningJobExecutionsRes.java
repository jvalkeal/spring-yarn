package org.springframework.yarn.batch.repository.bindings;

import java.util.Set;

public class FindRunningJobExecutionsRes extends BaseObject {

    public String message;
    public String status;
    public Set<JobExecutionType> jobExecutions;
    
    public FindRunningJobExecutionsRes() {
        super("FindRunningJobExecutionsRes");
    }

}
