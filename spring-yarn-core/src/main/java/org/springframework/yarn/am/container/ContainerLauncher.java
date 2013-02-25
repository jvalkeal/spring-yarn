package org.springframework.yarn.am.container;

import java.util.List;

import org.apache.hadoop.yarn.api.records.Container;

public interface ContainerLauncher {
    
    void launchContainer(Container lcontainer, List<String> commands);

}
