package org.springframework.yarn.integration.ip.mind;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.yarn.am.AppmasterService;
import org.springframework.yarn.am.AppmasterServiceClient;

/**
 * Integration tests around namespace configuration.
 * 
 * @author Janne Valkealahti
 * 
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class MindIntegrationDefaultTests {

    @Autowired
    ApplicationContext ctx;
        
    @Autowired
    AppmasterService appmasterService;

    @Autowired
    AppmasterServiceClient appmasterServiceClient;
        
    @Test
    public void testServiceInterfaces() throws Exception {
        assertNotNull(appmasterService);        
        assertNotNull(appmasterServiceClient);
        
        SimpleTestRequest request = new SimpleTestRequest();
        SimpleTestResponse response = (SimpleTestResponse) ((MindAppmasterServiceClient)appmasterServiceClient).doMindRequest(request);
        assertNotNull(response);        
        assertThat(response.stringField, is("echo:stringFieldValue"));
    }

}
