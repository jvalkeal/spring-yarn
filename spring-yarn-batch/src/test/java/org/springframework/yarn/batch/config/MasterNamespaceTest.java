package org.springframework.yarn.batch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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
