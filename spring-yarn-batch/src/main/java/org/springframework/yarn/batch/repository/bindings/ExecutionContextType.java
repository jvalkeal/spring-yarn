package org.springframework.yarn.batch.repository.bindings;

import java.util.Map;

import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

/**
 * Binding for {@link org.springframework.batch.item.ExecutionContext}.
 * We need to do a trick to also store clazz type of objects in
 * a map because otherwise json desirialization may take wrong
 * type i.e. between Integer and Long.
 * 
 * @author Janne Valkealahti
 *
 */
public class ExecutionContextType extends BaseObject {

    public Map<String, ObjectEntry> map;

    public ExecutionContextType() {
        super("ExecutionContextType");
    }
    
    public static class ObjectEntry {
        public Object obj;
        public String clazz;
        public ObjectEntry(){}
        public ObjectEntry(Object obj, String clazz) {
            this.obj = obj;
            this.clazz = clazz;
        }
    }

}
