package org.springframework.yarn.fs;

import java.util.Map;

import org.apache.hadoop.yarn.api.records.LocalResource;

/**
 * Interface for resource localizer implementation.
 * 
 * @author Janne Valkealahti
 *
 */
public interface ResourceLocalizer {
   
    /**
     * Gets a map of {@link LocalResource} instances.
     * @return The map containing {@link LocalResource} instances
     */
    Map<String, LocalResource> getResources();
    
    /**
     * If underlying implementation needs to do operations
     * on hdfs filesystem or any other preparation work,
     * calling of this method should make implementation
     * ready to return resources from {@link #getResources()}
     * command.
     */
    void distribute();

}
