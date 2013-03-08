package org.springframework.yarn.integration.support;

import org.springframework.integration.ip.tcp.connection.support.TcpSocketSupport;

public interface PortExposingTcpSocketSupport extends TcpSocketSupport {

    int getServerSocketPort();
    String getServerSocketAddress();
    
}
