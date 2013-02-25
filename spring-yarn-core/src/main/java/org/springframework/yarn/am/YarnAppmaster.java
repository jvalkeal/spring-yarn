package org.springframework.yarn.am;

import java.util.Properties;

/**
 * Interface defining main application master methods
 * needed for external launch implementations.
 * 
 * @author Janne Valkealahti
 *
 */
public interface YarnAppmaster {
    
    /**
     * Submit and run application.
     */
    void submitApplication();
    
    /**
     * Waiting application to reach its end.
     */
    void waitForCompletion();
    
    /**
     * Sets parameters for the application.
     * 
     * @param parameters the parameters to set
     */
    void setParameters(Properties parameters);
    
}
