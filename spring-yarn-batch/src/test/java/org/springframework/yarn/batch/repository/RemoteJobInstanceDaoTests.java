package org.springframework.yarn.batch.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.yarn.am.AppmasterServiceClient;

public class RemoteJobInstanceDaoTests extends AbstractRemoteJobInstanceDaoTests {

    @Autowired
    AppmasterServiceClient appmasterServiceClient;
    
    @Override
    protected RemoteJobInstanceDao getRemoteJobInstanceDao() {
        return new RemoteJobInstanceDao(new StubAppmasterScOperations());
    }    

}
