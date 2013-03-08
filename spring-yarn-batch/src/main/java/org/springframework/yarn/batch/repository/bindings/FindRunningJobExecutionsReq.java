package org.springframework.yarn.batch.repository.bindings;

public class FindRunningJobExecutionsReq extends BaseObject {

    public String jobName;
    
    public FindRunningJobExecutionsReq() {
        super("FindRunningJobExecutionsReq");
    }


}
