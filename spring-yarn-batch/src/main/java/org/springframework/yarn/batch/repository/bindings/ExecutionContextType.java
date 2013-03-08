package org.springframework.yarn.batch.repository.bindings;

import java.util.Map;

public class ExecutionContextType extends BaseObject {

    public Map<String, Object> map;

    public ExecutionContextType() {
        super("ExecutionContextType");
    }

}
