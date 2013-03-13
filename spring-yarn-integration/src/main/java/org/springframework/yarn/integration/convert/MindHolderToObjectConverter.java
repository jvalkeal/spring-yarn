package org.springframework.yarn.integration.convert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ClassUtils;
import org.springframework.yarn.integration.ip.mind.MindRpcMessageHolder;
import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Spring {@link Converter} which knows how to convert
 * {@link MindRpcMessageHolder} to {@link BaseObject}.
 * 
 * @author Janne Valkealahti
 *
 */
public class MindHolderToObjectConverter implements Converter<MindRpcMessageHolder, BaseObject> {

    /** Jackson object mapper */
    private ObjectMapper objectMapper;
    
    /** Base package prefix */
    private String basePackage;
    
    /** Simple class cache*/
    private Map<String, Class<? extends BaseObject>> classCache;
    
    /**
     * Constructs converter with a jackson object mapper and
     * a base package prefix.
     * 
     * @param objectMapper the object mapper
     * @param basePackage the base package
     */
    public MindHolderToObjectConverter(ObjectMapper objectMapper, String basePackage) {
        this.objectMapper = objectMapper;
        this.basePackage = basePackage;
        this.classCache = new ConcurrentHashMap<String, Class<? extends BaseObject>>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BaseObject convert(MindRpcMessageHolder source) {
        Map<String, String> headers = source.getHeaders();
        String type = headers.get("type").trim();
        byte[] content = source.getContent();

        try {
            String clazzName = basePackage + "." + type;
            Class<?> clazz = classCache.get(clazzName);
            if (clazz == null) {
                clazz = ClassUtils.resolveClassName(clazzName, getClass().getClassLoader());
                if (clazz != null) {
                    classCache.put(clazzName, (Class<? extends BaseObject>) clazz);
                }
            }
            BaseObject object = (BaseObject) objectMapper.readValue(content, clazz);
            return object;
        } catch (Exception e) {
            throw new MindDataConversionException("Failed to convert source object.", e);
        }
    }

}
