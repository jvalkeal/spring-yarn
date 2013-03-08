package org.springframework.yarn.integration.support;

import java.net.ServerSocket;
import java.net.Socket;

public class DefaultTcpSocketSupport implements PortExposingTcpSocketSupport {

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
