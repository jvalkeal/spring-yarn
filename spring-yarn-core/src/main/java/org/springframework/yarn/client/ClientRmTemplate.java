package org.springframework.yarn.client;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.ClientRMProtocol;
import org.apache.hadoop.yarn.api.protocolrecords.GetAllApplicationsRequest;
import org.apache.hadoop.yarn.api.protocolrecords.GetAllApplicationsResponse;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationRequest;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.protocolrecords.SubmitApplicationRequest;
import org.apache.hadoop.yarn.api.protocolrecords.SubmitApplicationResponse;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnRemoteException;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.yarn.rpc.YarnRpcAccessor;
import org.springframework.yarn.rpc.YarnRpcCallback;

/**
 * 
 * 
 * @author Janne Valkealahti
 *
 */
public class ClientRmTemplate extends YarnRpcAccessor<ClientRMProtocol> implements ClientRmOperations {

    public ClientRmTemplate(Configuration config) {
        super(ClientRMProtocol.class, config);
    }
    
    protected InetSocketAddress getRpcAddress(Configuration config) {
        return config.getSocketAddr(YarnConfiguration.RM_ADDRESS, YarnConfiguration.DEFAULT_RM_ADDRESS,
                YarnConfiguration.DEFAULT_RM_PORT);
    }
    
    @Override
    public List<ApplicationReport> listApplications() {
        return execute(new YarnRpcCallback<List<ApplicationReport>, ClientRMProtocol>() {
            @Override
            public List<ApplicationReport> doInYarn(ClientRMProtocol proxy) throws YarnRemoteException {
                GetAllApplicationsRequest request = Records.newRecord(GetAllApplicationsRequest.class);
                GetAllApplicationsResponse response = proxy.getAllApplications(request);
                return response.getApplicationList();
            }
        });
    }    

    @Override
    public GetNewApplicationResponse getNewApplication() {
        return execute(new YarnRpcCallback<GetNewApplicationResponse, ClientRMProtocol>() {
            @Override
            public GetNewApplicationResponse doInYarn(ClientRMProtocol proxy) throws YarnRemoteException {
                GetNewApplicationRequest request = Records.newRecord(GetNewApplicationRequest.class);
                return proxy.getNewApplication(request);
            }
        });
    }

    @Override
    public SubmitApplicationResponse submitApplication(final ApplicationSubmissionContext appSubContext) {
        return execute(new YarnRpcCallback<SubmitApplicationResponse, ClientRMProtocol>() {
            @Override
            public SubmitApplicationResponse doInYarn(ClientRMProtocol proxy) throws YarnRemoteException {
                SubmitApplicationRequest request = Records.newRecord(SubmitApplicationRequest.class);
                request.setApplicationSubmissionContext(appSubContext);
                return proxy.submitApplication(request);
            }
        });
    }

}
