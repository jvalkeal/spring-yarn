package org.springframework.yarn.batch.repository.bindings;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;


public class GetJobInstanceByIdReq extends BaseObject {

    public Long id;
    
    public GetJobInstanceByIdReq() {
        super("GetJobInstanceByIdReq");
    }

}
