package org.springframework.yarn.batch.repository;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.yarn.am.AppmasterServiceClient;
import org.springframework.yarn.integration.ip.mind.AppmasterMindScOperations;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("remotejobintegration-context.xml")
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class RemoteStepExecutionDaoIntegrationTests extends AbstractRemoteStepExecutionDaoTests {

    @Autowired
    AppmasterServiceClient appmasterServiceClient;
    
    @Override
    protected RemoteJobInstanceDao getRemoteJobInstanceDao() {
        return new RemoteJobInstanceDao((AppmasterMindScOperations) appmasterServiceClient);
    }

    @Override
    protected RemoteJobExecutionDao getRemoteJobExecutionDao() {
        return new RemoteJobExecutionDao((AppmasterMindScOperations) appmasterServiceClient);
    }

    @Override
    protected RemoteStepExecutionDao getRemoteStepExecutionDao() {
        return new RemoteStepExecutionDao((AppmasterMindScOperations) appmasterServiceClient);
    }

    @Override
    protected RemoteExecutionContextDao getRemoteExecutionContextDao() {
        return new RemoteExecutionContextDao((AppmasterMindScOperations) appmasterServiceClient);
    }    

}
