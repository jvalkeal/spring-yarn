package org.springframework.yarn.am;

import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.AMRMProtocol;
import org.apache.hadoop.yarn.api.protocolrecords.AllocateRequest;
import org.apache.hadoop.yarn.api.protocolrecords.AllocateResponse;
import org.apache.hadoop.yarn.api.protocolrecords.FinishApplicationMasterRequest;
import org.apache.hadoop.yarn.api.protocolrecords.FinishApplicationMasterResponse;
import org.apache.hadoop.yarn.api.protocolrecords.RegisterApplicationMasterRequest;
import org.apache.hadoop.yarn.api.protocolrecords.RegisterApplicationMasterResponse;
import org.apache.hadoop.yarn.api.records.ApplicationAttemptId;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnRemoteException;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.yarn.rpc.YarnRpcAccessor;
import org.springframework.yarn.rpc.YarnRpcCallback;

public class AppmasterRmTemplate extends YarnRpcAccessor<AMRMProtocol> implements AppmasterRmOperations {

    public AppmasterRmTemplate(Configuration config) {
        super(AMRMProtocol.class, config);
    }
    
    @Override
    public RegisterApplicationMasterResponse registerApplicationMaster(final ApplicationAttemptId appAttemptId, final String host, final Integer rpcPort, final String trackUrl) {
        return execute(new YarnRpcCallback<RegisterApplicationMasterResponse, AMRMProtocol>() {
            @Override
            public RegisterApplicationMasterResponse doInYarn(AMRMProtocol proxy) throws YarnRemoteException {
                RegisterApplicationMasterRequest appMasterRequest = Records.newRecord(RegisterApplicationMasterRequest.class);
                appMasterRequest.setApplicationAttemptId(appAttemptId);
                appMasterRequest.setHost(host != null ? host : "");
                appMasterRequest.setRpcPort(rpcPort != null ? rpcPort : 0);
                appMasterRequest.setTrackingUrl(trackUrl != null ? trackUrl : "");
                return proxy.registerApplicationMaster(appMasterRequest);
            }
        });
    }
    
    @Override
    public AllocateResponse allocate(final AllocateRequest request) {
        return execute(new YarnRpcCallback<AllocateResponse, AMRMProtocol>() {
            @Override
            public AllocateResponse doInYarn(AMRMProtocol proxy) throws YarnRemoteException {
                return proxy.allocate(request);
            }
        });
    }
    
    @Override
    public FinishApplicationMasterResponse finish(final FinishApplicationMasterRequest request) {
        return execute(new YarnRpcCallback<FinishApplicationMasterResponse, AMRMProtocol>() {
            @Override
            public FinishApplicationMasterResponse doInYarn(AMRMProtocol proxy) throws YarnRemoteException {
                return proxy.finishApplicationMaster(request);
            }
        });
    }
    
    protected InetSocketAddress getRpcAddress(Configuration config) {
        return config.getSocketAddr(YarnConfiguration.RM_SCHEDULER_ADDRESS,
                YarnConfiguration.DEFAULT_RM_SCHEDULER_ADDRESS, YarnConfiguration.DEFAULT_RM_SCHEDULER_PORT);        
    }    

}
