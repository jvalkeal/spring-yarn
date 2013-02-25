package org.springframework.yarn.am;

import org.apache.hadoop.yarn.api.protocolrecords.StartContainerRequest;
import org.apache.hadoop.yarn.api.protocolrecords.StartContainerResponse;

public interface AppmasterCmOperations {
    
    StartContainerResponse startContainer(StartContainerRequest request);

}
