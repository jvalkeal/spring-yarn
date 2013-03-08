package org.springframework.yarn.integration.ip.mind;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.yarn.am.AppmasterService;
import org.springframework.yarn.am.GenericRpcMessage;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.client.AppmasterScOperations;
import org.springframework.yarn.integration.support.PortExposingTcpSocketSupport;

/**
 * Tests for integrating {@link AppmasterService} and
 * {@link AppmasterScOperations} together. We're mostly testing that appmaster
 * service client is able to talk to appmaster service using the mind protocol.
 * 
 * @author Janne Valkealahti
 * 
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class MindIntegrationTests {

    @Autowired
    ApplicationContext ctx;
    
    @Autowired
    PortExposingTcpSocketSupport socketSupport;
    
    @Autowired
    MessageChannel serverChannel;

    @Autowired
    MessageChannel clientRequestChannel;

    @Autowired
    QueueChannel clientResponseChannel;
    
    @Autowired
    AppmasterService mindAppmasterService;

    @Autowired
    AppmasterScOperations mindAppmasterServiceClient;
    
    @Test
    public void testVanillaChannels() {
        assertNotNull(socketSupport);
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(new HashMap<String, String>(), "jee");
        clientRequestChannel.send(MessageBuilder.withPayload(holder).build());
        
        Message<?> receive = clientResponseChannel.receive();
        holder = (MindRpcMessageHolder) receive.getPayload();
        String content = new String(holder.getContent());
        assertNotNull(content);        
    }
    
//    @Test
    public void testServiceInterfaces() {
        assertNotNull(mindAppmasterService);        
        assertNotNull(mindAppmasterServiceClient);
        
        assertThat(mindAppmasterService.getPort(), greaterThan(0));
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(new HashMap<String, String>(), "jee");
        RpcMessage<MindRpcMessageHolder> request = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        
        RpcMessage<?> response = mindAppmasterServiceClient.get(request);
        assertNotNull(response);
        
    }

}
