package org.springframework.yarn.integration.ip.mind;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Mind request and response contains a set of headers and a content. This is
 * similar to http traffict. This holder keeps those separated and makes it
 * easier to work with payloads.
 * 
 * @author Janne Valkealahti
 * 
 */
public class MindRpcMessageHolder {

    private Map<String, String> headers;

    private byte[] content;

    public MindRpcMessageHolder() {
    }

    public MindRpcMessageHolder(Map<String, String> headers, String content) {
        super();
        this.headers = headers != null ? headers : new HashMap<String, String>();
        this.content = content.getBytes();
    }

    public MindRpcMessageHolder(Map<String, String> headers, byte[] content) {
        super();
        this.headers = headers != null ? headers : new HashMap<String, String>();
        this.content = content;
    }
    
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
    
    public void setContent(String content) {
        this.content = content.getBytes();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getContent() {
        return content;
    }

    public byte[] toBytes() {
        StringBuilder buf = new StringBuilder();
        for (Entry<String, String> entry : headers.entrySet()) {
            buf.append(entry.getKey());
            buf.append(": ");
            buf.append(entry.getValue());
            buf.append("\r\n");
        }
        buf.append("\r\n");
        int l1 = buf.length();
        buf.insert(0, "MRPC/2 " + l1 + " " + content.length + "\r\n");
        byte[] bytes1 = buf.toString().getBytes();
        byte[] combined = new byte[content.length+bytes1.length];
        System.arraycopy(bytes1, 0, combined, 0, bytes1.length);
        System.arraycopy(content, 0, combined, bytes1.length, content.length);
        return combined;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Holder headers={");
        for(Entry<String, String> entry : headers.entrySet()) {
            buf.append(entry.getKey());            
            buf.append("=");
            buf.append(entry.getValue());            
            buf.append(",");
        }
        buf.append("} Content size=");
        buf.append(content != null ? content.length : 0);
        buf.append(", Content=");
        buf.append(content != null ? new String(content) : "");
        return buf.toString();
    }
    
    

}
