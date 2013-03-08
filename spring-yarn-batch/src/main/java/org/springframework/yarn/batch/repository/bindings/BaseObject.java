package org.springframework.yarn.batch.repository.bindings;

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
