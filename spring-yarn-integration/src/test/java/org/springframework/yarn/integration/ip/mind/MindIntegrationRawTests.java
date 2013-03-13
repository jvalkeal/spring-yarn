package org.springframework.yarn.integration.ip.mind;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
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
import org.springframework.yarn.client.AppmasterScOperations;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;
import org.springframework.yarn.integration.support.JacksonUtils;
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
public class MindIntegrationRawTests {

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
    MindAppmasterServiceClient mindAppmasterServiceClient;
    
    @Test
    public void testVanillaChannels() throws Exception {
        assertNotNull(socketSupport);
        
        SimpleTestRequest req = new SimpleTestRequest();        
        ObjectMapper objectMapper = JacksonUtils.getObjectMapper();
        String content = objectMapper.writeValueAsString(req);
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "SimpleTestRequest");
        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, content);
        clientRequestChannel.send(MessageBuilder.withPayload(holder).build());
        
        Message<?> receive = clientResponseChannel.receive();
        holder = (MindRpcMessageHolder) receive.getPayload();
        String contentRes = new String(holder.getContent());
        assertNotNull(contentRes);
    }
    
    @Test
    public void testServiceInterfaces() throws Exception {
        assertNotNull(mindAppmasterService);        
        assertNotNull(mindAppmasterServiceClient);
        
        assertThat(mindAppmasterService.getPort(), greaterThan(0));

        SimpleTestRequest request = new SimpleTestRequest();        
        BaseResponseObject response = mindAppmasterServiceClient.doMindRequest(request);
        assertNotNull(response);        
    }

}
