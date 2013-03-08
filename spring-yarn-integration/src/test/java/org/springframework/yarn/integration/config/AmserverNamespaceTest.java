package org.springframework.yarn.integration.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.yarn.integration.IntegrationAppmasterServiceFactoryBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("amserver-ns.xml")
public class AmserverNamespaceTest {

    @Autowired
    private ApplicationContext ctx;
    
    @Test
    public void testSomething() {
        assertTrue(ctx.containsBean("yarnAmserver"));
        IntegrationAppmasterServiceFactoryBean ycfb = (IntegrationAppmasterServiceFactoryBean) ctx.getBean("&yarnAmserver");
        assertNotNull(ycfb);
    }

}
