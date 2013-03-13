package org.springframework.yarn.integration.config;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.yarn.TestUtils;
import org.springframework.yarn.integration.IntegrationAppmasterServiceClientFactoryBean;
import org.springframework.yarn.integration.ip.mind.TestServiceClient;

/**
 * Testing 'int:amservice-client' namespace.
 * 
 * @author Janne Valkealahti
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("amserviceclient-ns.xml")
public class AmserviceClientNamespaceTest {

    @Autowired
    private ApplicationContext ctx;
    
    @Test
    public void testDefaults() throws Exception {
        assertTrue(ctx.containsBean("yarnAmserviceClient"));
        IntegrationAppmasterServiceClientFactoryBean fb =
                (IntegrationAppmasterServiceClientFactoryBean) ctx.getBean("&yarnAmserviceClient");
        assertNotNull(fb);
        
        Class<?> clazz = TestUtils.readField("serviceImpl", fb);
        assertThat(clazz.getCanonicalName(), is("org.springframework.yarn.integration.ip.mind.TestServiceClient"));
        assertThat(TestUtils.readField("objectMapper", fb), nullValue());
        assertThat(TestUtils.readField("requestChannel", fb), instanceOf(DirectChannel.class));
        assertThat(TestUtils.readField("responseChannel", fb), instanceOf(QueueChannel.class));
        assertThat(TestUtils.readField("serviceClient", fb), instanceOf(TestServiceClient.class));
        
        TestServiceClient client = (TestServiceClient) ctx.getBean("yarnAmserviceClient");
        assertNotNull(client);
        assertThat(TestUtils.readField("requestChannel", client), instanceOf(DirectChannel.class));
        assertThat(TestUtils.readField("responseChannel", client), instanceOf(QueueChannel.class));
        assertThat(TestUtils.readField("objectMapper", client), nullValue());        
    }
    
    @Test
    public void testChannelBeans() throws Exception {
        assertTrue(ctx.containsBean("yarnAmserviceClient2"));
        IntegrationAppmasterServiceClientFactoryBean fb =
                (IntegrationAppmasterServiceClientFactoryBean) ctx.getBean("&yarnAmserviceClient2");
        assertNotNull(fb);

        Class<?> clazz = TestUtils.readField("serviceImpl", fb);
        assertThat(clazz.getCanonicalName(), is("org.springframework.yarn.integration.ip.mind.TestServiceClient"));
        assertThat(TestUtils.readField("objectMapper", fb), nullValue());
        assertThat(TestUtils.readField("requestChannel", fb), instanceOf(DirectChannel.class));
        assertThat(TestUtils.readField("responseChannel", fb), instanceOf(QueueChannel.class));
        assertThat(TestUtils.readField("serviceClient", fb), instanceOf(TestServiceClient.class));
        
        TestServiceClient client = (TestServiceClient) ctx.getBean("yarnAmserviceClient2");
        assertNotNull(client);
        assertThat(TestUtils.readField("requestChannel", client), instanceOf(DirectChannel.class));
        assertThat(TestUtils.readField("responseChannel", client), instanceOf(QueueChannel.class));
        assertThat(TestUtils.readField("objectMapper", client), nullValue());  
    }

}
