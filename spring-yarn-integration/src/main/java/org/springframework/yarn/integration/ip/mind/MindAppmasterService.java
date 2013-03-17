package org.springframework.yarn.integration.ip.mind;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.yarn.am.AppmasterService;
import org.springframework.yarn.am.GenericRpcMessage;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.integration.IntegrationAppmasterService;

/**
 * Implementation of {@link AppmasterService} which handles communication
 * via Spring Int tcp channels using mind protocol.
 * 
 * @author Janne Valkealahti
 *
 */
public abstract class MindAppmasterService extends IntegrationAppmasterService<MindRpcMessageHolder> {

    private static final Log log = LogFactory.getLog(MindAppmasterService.class);
    
    @Override
    public RpcMessage<MindRpcMessageHolder> handleMessageInternal(RpcMessage<MindRpcMessageHolder> message) {
        try {
            return new GenericRpcMessage<MindRpcMessageHolder>(handleRpcMessage(message.getBody()));
        } catch (Exception e) {
            // TODO: need to handle this error
            log.error("error", e);
        }
        return null;
    }

    protected abstract MindRpcMessageHolder handleRpcMessage(MindRpcMessageHolder message) throws Exception;
        
}
