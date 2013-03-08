package org.springframework.yarn.integration.ip.mind;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.integration.test.util.SocketUtils;
import org.springframework.yarn.integration.support.JacksonUtils;

public class MindSerializationTests {

    private static ObjectMapper mapper = JacksonUtils.getObjectMapper();
        
    @Test
    public void testEmpty() throws Exception {
        final int port = SocketUtils.findAvailableServerSocket();
        final String testString = "abcdef";
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
    

    /**
     * @param is
     * @param buff
     */
    private void readFully(InputStream is, byte[] buff) throws IOException {
        for (int i = 0; i < buff.length; i++) {
            buff[i] = (byte) is.read();
        }
    }
    
}
