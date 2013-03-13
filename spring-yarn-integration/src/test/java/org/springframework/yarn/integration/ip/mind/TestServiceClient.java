package org.springframework.yarn.integration.ip.mind;

import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Dummy service client.
 * 
 * @author Janne Valkealahti
 *
 */
public class TestServiceClient extends MindAppmasterServiceClient {

    public TestServiceClient() {
    }
    
    @Override
    public MindRpcMessageHolder getPayload(RpcMessage<?> message) {
        return null;
    }

    @Override
    protected BaseResponseObject getBaseResponseObject(RpcMessage<?> rpcMessage) {
        return null;
    }    

}
