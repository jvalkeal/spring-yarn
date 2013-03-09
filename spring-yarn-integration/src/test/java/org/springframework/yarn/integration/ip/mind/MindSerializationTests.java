package org.springframework.yarn.integration.ip.mind;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import org.junit.Test;
import org.springframework.integration.test.util.SocketUtils;

public class MindSerializationTests {

    @Test
    public void testEmpty() throws Exception {
        final int port = SocketUtils.findAvailableServerSocket();
        ServerSocket server = ServerSocketFactory.getDefault().createServerSocket(port);
        server.setSoTimeout(10000);
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = SocketFactory.getDefault().createSocket("localhost", port);                    
                    MindRpcMessageHolder holder = new MindRpcMessageHolder(new HashMap<String, String>(), "jee");
                    MindRpcSerializer serializer = new MindRpcSerializer();
                    serializer.serialize(holder, socket.getOutputStream());
                    Thread.sleep(1000000000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
        Socket socket = server.accept();
        socket.setSoTimeout(5000);
        InputStream is = socket.getInputStream();
        MindRpcSerializer serializer = new MindRpcSerializer();
        MindRpcMessageHolder holder = serializer.deserialize(is);
        String content = new String(holder.getContent());
        server.close();
        assertNotNull(content);
        assertNotNull(holder);
    }
        
}
