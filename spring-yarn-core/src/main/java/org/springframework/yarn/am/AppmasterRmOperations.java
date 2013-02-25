package org.springframework.yarn.am;

import org.apache.hadoop.yarn.api.protocolrecords.AllocateRequest;
import org.apache.hadoop.yarn.api.protocolrecords.AllocateResponse;
import org.apache.hadoop.yarn.api.protocolrecords.FinishApplicationMasterRequest;
import org.apache.hadoop.yarn.api.protocolrecords.FinishApplicationMasterResponse;
import org.apache.hadoop.yarn.api.protocolrecords.RegisterApplicationMasterResponse;
import org.apache.hadoop.yarn.api.records.ApplicationAttemptId;

public interface AppmasterRmOperations {

    RegisterApplicationMasterResponse registerApplicationMaster(ApplicationAttemptId appAttemptId, String host, Integer rpcPort, String trackUrl);
    
    AllocateResponse allocate(AllocateRequest request);
    
    FinishApplicationMasterResponse finish(FinishApplicationMasterRequest request);
    
}
