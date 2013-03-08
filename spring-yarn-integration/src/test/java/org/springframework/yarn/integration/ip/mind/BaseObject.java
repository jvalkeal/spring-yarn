package org.springframework.yarn.integration.ip.mind;

public abstract class BaseObject {

    public String type;

    public BaseObject() {
    }

    public BaseObject(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
