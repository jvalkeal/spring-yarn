package org.springframework.yarn.am;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.yarn.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AppmasterTests {

    @Autowired
    private ApplicationContext ctx;
    
    @Test
    public void testAppmaster() throws Exception {
        assertTrue(ctx.containsBean("yarnAppmaster"));
        AppmasterFactoryBean fb = (AppmasterFactoryBean) ctx.getBean("&yarnAppmaster");
        assertNotNull(fb);
        
        YarnAppmaster master = (YarnAppmaster) ctx.getBean("yarnAppmaster");
        assertNotNull(master);
        
//        AppmasterService service = TestUtils.readField("appmasterService", master);
//        assertThat(service, notNullValue());        

//        ReflectionTestUtils.invokeMethod(master, "getAppmasterService", new Object[0]);
        
    }
    
    public static class StubAppmasterService implements AppmasterService {
        @Override
        public int getPort() {
            return 0;
        }
        @Override
        public String getHost() {
            return null;
        }
        @Override
        public boolean hasPort() {
            return true;
        }        
    }
    

}
