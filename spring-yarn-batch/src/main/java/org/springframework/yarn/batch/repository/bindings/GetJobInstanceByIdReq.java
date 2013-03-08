package org.springframework.yarn.batch.repository.bindings;


public class GetJobInstanceByIdReq extends BaseObject {

    public Long id;
    
    public GetJobInstanceByIdReq() {
        super("GetJobInstanceByIdReq");
    }

}
