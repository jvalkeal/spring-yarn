package org.springframework.yarn.integration.ip.mind;

public class ErrorResponse extends BaseObject {

    public String code;

    public String text;

    public ErrorResponse() {
        super("errorResponse");
    }

}
