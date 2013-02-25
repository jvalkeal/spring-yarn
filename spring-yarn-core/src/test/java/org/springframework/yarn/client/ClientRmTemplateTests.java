package org.springframework.yarn.client;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.apache.hadoop.yarn.api.ClientRMProtocol;
import org.apache.hadoop.yarn.api.protocolrecords.GetAllApplicationsRequest;
import org.apache.hadoop.yarn.api.protocolrecords.GetAllApplicationsResponse;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.exceptions.YarnRemoteException;
import org.apache.hadoop.yarn.util.Records;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.yarn.rpc.YarnRpcCallback;

/**
 * Tests for {@link ClientRmTemplate}.
 * 
 * @author Janne Valkealahti
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ClientRmTemplateTests {

    @Resource(name = "yarnClientRmTemplate")
    private ClientRmTemplate template;

    @Test
    public void testGetNewApplicationResponse() {
        assertNotNull(template);
        
        GetNewApplicationResponse response = template.getNewApplication();
        assertNotNull(response);        
    }
    
    @Test
    public void testListApplications() {
        List<ApplicationReport> applications = template.listApplications();
        assertNotNull(applications);        
    }
    
    @Test
    public void testExecuteCallback() {
        List<ApplicationReport> applications = template.execute(new YarnRpcCallback<List<ApplicationReport>, ClientRMProtocol>() {
            @Override
            public List<ApplicationReport> doInYarn(ClientRMProtocol proxy) throws YarnRemoteException {
                GetAllApplicationsRequest request = Records.newRecord(GetAllApplicationsRequest.class);
                GetAllApplicationsResponse response = proxy.getAllApplications(request);
                return response.getApplicationList();
            }
        });
        assertNotNull(applications);        
    }

}
