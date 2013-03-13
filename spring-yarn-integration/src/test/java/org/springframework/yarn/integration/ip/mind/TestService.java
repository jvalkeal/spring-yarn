package org.springframework.yarn.integration.ip.mind;

import org.springframework.core.convert.ConversionService;
import org.springframework.yarn.integration.ip.mind.binding.BaseObject;

/**
 * Custom testing service responding requests send from
 * a {@link TestServiceClient}. 
 * 
 * @author Janne Valkealahti
 *
 */
public class TestService extends MindAppmasterService {

    @Override
    protected MindRpcMessageHolder handleRpcMessage(MindRpcMessageHolder message) throws Exception {
        
        ConversionService conversionService = getConversionService();
        SimpleTestRequest request = (SimpleTestRequest) conversionService.convert(message, BaseObject.class);
        
        SimpleTestResponse response = new SimpleTestResponse();
        response.stringField = "echo:" + request.stringField;
        
        MindRpcMessageHolder holder = conversionService.convert(response, MindRpcMessageHolder.class);
        return holder;
        
    }    

}
