package org.springframework.yarn.client;

import java.util.List;

import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.protocolrecords.SubmitApplicationResponse;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;

/**
 * Interface for client to resource manager communication.
 * 
 * @author Janne Valkealahti
 *
 */
public interface ClientRmOperations {
    
    GetNewApplicationResponse getNewApplication();
    SubmitApplicationResponse submitApplication(ApplicationSubmissionContext appSubContext);
    List<ApplicationReport> listApplications();

}
