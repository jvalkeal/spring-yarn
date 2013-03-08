package org.springframework.yarn.integration.ip.mind;

public class LoginRequest extends BaseObject {

    public String user;

    public String password;

    public LoginRequest(String user, String password) {
        super("loginRequest");
        this.user = user;
        this.password = password;
    }

}
