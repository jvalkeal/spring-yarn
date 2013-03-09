package org.springframework.yarn.integration;

import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.PollableChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.yarn.am.GenericRpcMessage;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.client.AppmasterScOperations;
import org.springframework.yarn.integration.ip.mind.MindRpcMessageHolder;
import org.springframework.yarn.support.LifecycleObjectSupport;

/**
 * 
 * 
 * @author Janne Valkealahti
 *
 */
public abstract class IntegrationAppmasterServiceClient extends LifecycleObjectSupport implements AppmasterScOperations {

    private MessageChannel requestChannel;
    private PollableChannel responseChannel;
    
    public IntegrationAppmasterServiceClient() {
    }

    @Override
    protected void doStart() {
    }

    @Override
    protected void doStop() {
    }
    
    public void setRequestChannel(MessageChannel requestChannel) {
        this.requestChannel = requestChannel;
    }

    public void setResponseChannel(PollableChannel responseChannel) {
        this.responseChannel = responseChannel;
    }

    @Override
    public RpcMessage<?> get(RpcMessage<?> message) {
        requestChannel.send(MessageBuilder.withPayload(message.getBody()).build());
        Object payload = responseChannel.receive().getPayload();
        return new GenericRpcMessage<MindRpcMessageHolder>((MindRpcMessageHolder) payload);
//        return (RpcMessage<?>) responseChannel.receive().getPayload();
    }
    
//    protected abstract RpcMessage<?> convert(RpcMessage<?> message);

    
}
