package org.springframework.yarn.client;

import java.util.List;

import org.apache.hadoop.yarn.api.records.ApplicationReport;

/**
 * Interface for Spring Yarn facing client methods.
 * 
 * @author Janne Valkealahti
 *
 */
public interface YarnClient {

    void submitApplication();
    
    List<ApplicationReport> listApplications();
    
}
