package org.springframework.yarn.integration.ip.mind;

import org.springframework.yarn.client.AppmasterScOperations;
import org.springframework.yarn.integration.ip.mind.binding.BaseObject;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

public interface AppmasterMindScOperations extends AppmasterScOperations {

    BaseResponseObject doMindRequest(BaseObject request);
    
}
