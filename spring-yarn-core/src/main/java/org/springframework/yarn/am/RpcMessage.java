package org.springframework.yarn.am;

/**
 * The central interface that any RpcMessage type must implement.
 * 
 * @author Janne Valkealahti
 *
 * @param <T> Type of the message
 */
public interface RpcMessage<T> {

    /**
     * Gets a body of this message.
     * 
     * @return wrapped body of the message
     */
    T getBody();
    
}
