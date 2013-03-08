package org.springframework.yarn.client;

import org.springframework.yarn.am.RpcMessage;

/**
 * Interface defining message operations for Application
 * Master Service Client.
 * 
 * @author Janne Valkealahti
 *
 */
public interface AppmasterScOperations {
    
    RpcMessage<?> get(RpcMessage<?> message);

}
