package org.springframework.yarn.integration.ip.mind;

public class LoginService {

    public MindRpcMessageHolder login(MindRpcMessageHolder s) {
        String content = new String(s.getContent());
        content = "echo " + content;
        s.setContent(content);
        return s;
    }    

}
