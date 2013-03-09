package org.springframework.yarn.integration.support;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class JacksonUtils {

    @SuppressWarnings("deprecation")
    public static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
//        SerializationConfig serializationconfig = mapper
//                .getSerializationConfig().withSerializationInclusion(Inclusion.NON_NULL);
//        mapper.setSerializationConfig(serializationconfig);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

}
