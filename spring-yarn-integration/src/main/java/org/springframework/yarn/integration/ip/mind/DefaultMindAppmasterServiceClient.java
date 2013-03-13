package org.springframework.yarn.integration.ip.mind;

import org.springframework.core.convert.ConversionService;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.integration.ip.mind.binding.BaseObject;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Default implementation of {@link MindAppmasterServiceClient} which
 * handles the message serialization using type information in
 * mind message headers.
 * 
 * @author Janne Valkealahti
 *
 */
public class DefaultMindAppmasterServiceClient extends MindAppmasterServiceClient {

    @Override
    protected BaseResponseObject getBaseResponseObject(RpcMessage<?> rpcMessage) {
        MindRpcMessageHolder holder = (MindRpcMessageHolder) rpcMessage.getBody();
        ConversionService conversionService = getConversionService();
        if(conversionService != null) {
            return (BaseResponseObject) conversionService.convert(holder, BaseObject.class);
        }
        return null;
    }
    
}
