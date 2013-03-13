package org.springframework.yarn.integration.ip.mind;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.yarn.am.GenericRpcMessage;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.integration.IntegrationAppmasterServiceClient;
import org.springframework.yarn.integration.ip.mind.binding.BaseObject;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of Appmaster service client extending
 * {@link IntegrationAppmasterServiceClient}.
 * 
 * @author Janne Valkealahti
 *
 */
public abstract class MindAppmasterServiceClient extends IntegrationAppmasterServiceClient<MindRpcMessageHolder> {

    private static final Log log = LogFactory.getLog(MindAppmasterServiceClient.class);
    
    private ObjectMapper objectMapper;
    
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public BaseResponseObject doMindRequest(BaseObject request) {
        
        try {            
            GenericRpcMessage<BaseObject> message = new GenericRpcMessage<BaseObject>(request);
            RpcMessage<?> rpcMessage = get(message);
            return getBaseResponseObject(rpcMessage);
        } catch (Exception e) {
            // TODO: handle error
            log.error("error", e);
        }
        
        return null;
    }
    
    /**
     * This method is called from {@link #doMindRequest(BaseObject)} to convert
     * the response content back to mind {@link BaseResponseObject}. This class
     * itself can't have enough knowledge to do the conversion so process
     * is left to implementor.
     * 
     * @param rpcMessage the rpc message
     * @return the {@link BaseResponseObject} build from a {@link RpcMessage}
     */
    protected abstract BaseResponseObject getBaseResponseObject(RpcMessage<?> rpcMessage);

    @Override
    public MindRpcMessageHolder getPayload(RpcMessage<?> message) {
        Object body = message.getBody();
        if(body instanceof MindRpcMessageHolder) {
            return (MindRpcMessageHolder)body;
        }
        ConversionService conversionService = getConversionService();
        if(conversionService != null) {
            return conversionService.convert(body, MindRpcMessageHolder.class);
        }
        return null;
    }
    
    public <T> T convert(MindRpcMessageHolder holder, Class<T> clazz) throws Exception {
        return objectMapper.readValue(holder.getContent(), clazz);
    }

    public String convert(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
    
    
}
