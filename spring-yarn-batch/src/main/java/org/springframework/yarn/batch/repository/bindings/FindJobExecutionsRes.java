package org.springframework.yarn.batch.repository.bindings;

import java.util.List;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class FindJobExecutionsRes extends BaseObject {

    public String message;
    public String status;
    public List<JobExecutionType> jobExecutions;
    
    public FindJobExecutionsRes() {
        super("FindJobExecutionsRes");
    }

}
