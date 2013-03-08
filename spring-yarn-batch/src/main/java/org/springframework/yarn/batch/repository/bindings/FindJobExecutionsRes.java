package org.springframework.yarn.batch.repository.bindings;

import java.util.List;

public class FindJobExecutionsRes extends BaseObject {

    public String message;
    public String status;
    public List<JobExecutionType> jobExecutions;
    
    public FindJobExecutionsRes() {
        super("FindJobExecutionsRes");
    }

}
