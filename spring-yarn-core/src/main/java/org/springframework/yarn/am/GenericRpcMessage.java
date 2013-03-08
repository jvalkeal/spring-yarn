package org.springframework.yarn.am;

/**
 * Generic {@link RpcMessage} implementation.
 * 
 * @author Janne Valkealahti
 *
 * @param <T> Type of the message body
 */
public class GenericRpcMessage<T> implements RpcMessage<T> {

    private final T body;
    
    /**
     * Constructs a {@link GenericRpcMessage} with a given body.
     * 
     * @param body The body
     */
    public GenericRpcMessage(T body) {
        this.body = body;
    }

    @Override
    public T getBody() {
        return body;
    }

}
