package org.springframework.yarn.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.yarn.client.YarnClientFactoryBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("client-ns.xml")
public class ClientNamespaceTest {

    @Autowired
    private ApplicationContext ctx;
    
    @Test
    public void testSomething() {
        assertTrue(ctx.containsBean("yarnClient"));
        YarnClientFactoryBean ycfb = (YarnClientFactoryBean) ctx.getBean("&yarnClient");
        assertNotNull(ycfb);
    }

}
