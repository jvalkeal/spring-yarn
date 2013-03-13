package org.springframework.yarn.integration.ip.mind.binding;

/**
 * Base object of messages meant for pojo mapping.
 * 
 * @author Janne Valkealahti
 *
 */
public abstract class BaseObject {

    /** Type identifier of the object */
    public String type;

    /**
     * Constructs an empty object
     */
    public BaseObject() {
    }

    /**
     * Constructs object with a given type
     * @param type the type identifier
     */
    public BaseObject(String type) {
        this.type = type;
    }

    /**
     * Get type of this object.
     * @return type of the object
     */
    public String getType() {
        return type;
    }

}
