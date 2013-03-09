package org.springframework.yarn.batch.repository.bindings;

/**
 * Base object for messages which should have a common
 * fields for responses. Fields can be used to tell
 * state and error of a response.
 * 
 * @author Janne Valkealahti
 *
 */
public class BaseResponseObject extends BaseObject {

    /** Message of a response. */
    public String resmsg;

    /** State of a response. */
    public String resstate;
    
    public BaseResponseObject() {
    }

    public BaseResponseObject(String type) {
        super(type);
    }

    public BaseResponseObject(String type, String resmsg, String resstate) {
        super(type);
        this.resmsg = resmsg;
        this.resstate = resstate;
    }

    public String getResmsg() {
        return resmsg;
    }

    public void setResmsg(String resmsg) {
        this.resmsg = resmsg;
    }

    public String getResstate() {
        return resstate;
    }

    public void setResstate(String resstate) {
        this.resstate = resstate;
    }
    
}
