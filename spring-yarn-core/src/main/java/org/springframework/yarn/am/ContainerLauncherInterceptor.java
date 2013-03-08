package org.springframework.yarn.am;

import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;

public interface ContainerLauncherInterceptor {

    ContainerLaunchContext preLaunch(ContainerLaunchContext context);
    
}
