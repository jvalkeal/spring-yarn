package org.springframework.yarn.integration.support;

import org.springframework.integration.ip.tcp.connection.support.TcpSocketSupport;

/**
 * Extension of {@link TcpSocketSupport} interface adding methods
 * to get more information about the socket ports.
 * 
 * @author Janne Valkealahti
 *
 */
public interface PortExposingTcpSocketSupport extends TcpSocketSupport {

    /**
     * Gets the binded server socket port.
     * 
     * @return the server socket port
     */
    int getServerSocketPort();
    
    /**
     * Gets the binded server socket address.
     * 
     * @return the server socket address
     */
    String getServerSocketAddress();
    
}
