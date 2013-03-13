package org.springframework.yarn.integration.support;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Implementation of {@link org.springframework.integration.ip.tcp.connection.support.TcpSocketSupport}
 * which extends its base functionality by catching socket information, like listen address and port.
 * 
 * @author Janne Valkealahti
 *
 */
public class DefaultPortExposingTcpSocketSupport implements PortExposingTcpSocketSupport {

    private int serverSocketPort = -1;
    private String serverSocketAddress;
    
    @Override
    public void postProcessServerSocket(ServerSocket serverSocket) {
        serverSocketPort = serverSocket.getLocalPort();
        serverSocketAddress = serverSocket.getInetAddress().getHostAddress();
    }

    @Override
    public void postProcessSocket(Socket socket) {
    }

    @Override
    public int getServerSocketPort() {
        return serverSocketPort;
    }

    @Override
    public String getServerSocketAddress() {
        return serverSocketAddress;
    }


}
