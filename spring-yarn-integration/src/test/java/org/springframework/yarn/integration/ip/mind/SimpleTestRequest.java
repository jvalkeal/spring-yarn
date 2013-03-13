package org.springframework.yarn.integration.ip.mind;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

public class SimpleTestRequest extends BaseObject {

    public String stringField;
    public String nullStringField;
    
    public SimpleTestRequest() {
        super("SimpleTestRequest");
        stringField = "stringFieldValue";
    }

}
