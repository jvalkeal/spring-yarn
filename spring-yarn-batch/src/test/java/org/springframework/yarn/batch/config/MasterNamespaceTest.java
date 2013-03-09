package org.springframework.yarn.batch.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.yarn.batch.am.BatchAppmasterFactoryBean;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("master-ns.xml")
public class MasterNamespaceTest {

    @Autowired
    private ApplicationContext ctx;
    
//    @Test
//    public void testSomething() {
//        assertTrue(ctx.containsBean("yarnAppmaster"));
//        BatchAppmasterFactoryBean fb = (BatchAppmasterFactoryBean) ctx.getBean("&yarnAppmaster");
//        assertNotNull(fb);
//    }

}
