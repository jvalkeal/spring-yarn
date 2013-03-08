package org.springframework.yarn.batch.repository.bindings;


public class FindJobExecutionsReq extends BaseObject {

    public JobInstanceType jobInstance;
    
    public FindJobExecutionsReq() {
        super("FindJobExecutionsReq");
    }

}
