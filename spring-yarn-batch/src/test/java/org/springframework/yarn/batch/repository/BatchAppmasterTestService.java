package org.springframework.yarn.batch.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.yarn.integration.ip.mind.MindAppmasterService;
import org.springframework.yarn.integration.ip.mind.MindRpcMessageHolder;
import org.springframework.yarn.integration.ip.mind.binding.BaseObject;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Application master service implementation which is used for
 * remote Spring Batch steps to talk to Job Repository. Simply
 * passes requests to {@link JobRepositoryRemoteService}.
 * 
 * @author Janne Valkealahti
 *
 */
public class BatchAppmasterTestService extends MindAppmasterService {

    private static final Log log = LogFactory.getLog(BatchAppmasterTestService.class);
    
    private JobRepositoryRemoteService jobRepositoryRemoteService;

    @Override
    protected MindRpcMessageHolder handleRpcMessage(MindRpcMessageHolder message) throws Exception {
        if(log.isDebugEnabled()) {
            log.debug("Incoming MindRpcMessageHolder: " + message);
        }
        
        BaseObject baseObject = getConversionService().convert(message, BaseObject.class);
        BaseResponseObject baseResponseObject = jobRepositoryRemoteService.get(baseObject);       
        MindRpcMessageHolder out = getConversionService().convert(baseResponseObject, MindRpcMessageHolder.class);
        
        if(log.isDebugEnabled()) {
            log.debug("Outgoing MindRpcMessageHolder: " + message);
        }
        
        return out;
    }
    
    public void setJobRepositoryRemoteService(JobRepositoryRemoteService jobRepositoryRemoteService) {
        this.jobRepositoryRemoteService = jobRepositoryRemoteService;
    }


}
