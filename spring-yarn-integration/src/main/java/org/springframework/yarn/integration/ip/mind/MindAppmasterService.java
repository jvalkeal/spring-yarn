package org.springframework.yarn.integration.ip.mind;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.yarn.integration.IntegrationAppmasterService;

/**
 * 
 * 
 * @author Janne Valkealahti
 *
 */
public class MindAppmasterService extends IntegrationAppmasterService {

    private static final Log log = LogFactory.getLog(MindAppmasterService.class);
    
    public MindAppmasterService() {
        log.info("MindAppmasterService constructor");
    }
    
//    public MindRpcMessageHolder handleMessage(MindRpcMessageHolder holder) {
//        Map<String, String> headers = holder.getHeaders();
//        String content = new String(holder.getContent());
//        content = "echo " + content;
//        holder.setContent(content);
//        return holder;
//    }

    @Override
    public boolean hasPort() {
        return true;
    }

}
