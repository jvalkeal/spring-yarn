package org.springframework.yarn.integration.ip.mind;

import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

public class SimpleTestResponse extends BaseResponseObject {

    public String stringField;
    public String nullStringField;
    
    public SimpleTestResponse() {
        super("SimpleTestResponse");
    }
    

}
