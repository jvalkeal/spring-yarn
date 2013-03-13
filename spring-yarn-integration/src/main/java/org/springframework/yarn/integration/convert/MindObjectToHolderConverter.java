package org.springframework.yarn.integration.convert;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.yarn.integration.ip.mind.MindRpcMessageHolder;
import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Spring {@link Converter} which knows how to convert
 * {@link BaseObject} to {@link MindRpcMessageHolder}.
 * 
 * @author Janne Valkealahti
 *
 */
public class MindObjectToHolderConverter implements Converter<BaseObject, MindRpcMessageHolder> {

    /** Jackson object mapper */
    private ObjectMapper objectMapper;
    
    /**
     * Constructs converter with a jackson object mapper.
     * 
     * @param objectMapper the object mapper
     */
    public MindObjectToHolderConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public MindRpcMessageHolder convert(BaseObject source) {
        try {
            String type = source.getType();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("type", type);
            byte[] content = objectMapper.writeValueAsBytes(source);        
            return new MindRpcMessageHolder(headers, content);
        } catch (JsonProcessingException e) {
            throw new MindDataConversionException("Failed to convert source object.", e);
        }
    }

}
