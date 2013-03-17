package org.springframework.yarn.batch.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.yarn.am.AppmasterServiceClient;

public class RemoteJobExecutionDaoTests extends AbstractRemoteJobExecutionDaoTests {

    @Autowired
    AppmasterServiceClient appmasterServiceClient;
    
    @Override
    protected RemoteJobInstanceDao getRemoteJobInstanceDao() {
        return new RemoteJobInstanceDao(new StubAppmasterScOperations());
    }

    @Override
    protected RemoteJobExecutionDao getRemoteJobExecutionDao() {
        return new RemoteJobExecutionDao(new StubAppmasterScOperations());
    }

    @Override
    protected RemoteStepExecutionDao getRemoteStepExecutionDao() {
        return new RemoteStepExecutionDao(new StubAppmasterScOperations());
    }

}
