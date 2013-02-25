package org.springframework.yarn.am.container;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.StartContainerRequest;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.yarn.am.AppmasterCmTemplate;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * 
 * 
 * @author Janne Valkealahti
 *
 */
public class DefaultContainerLauncher implements ContainerLauncher {

    private static final Log log = LogFactory.getLog(DefaultContainerLauncher.class);
    
    private Configuration configuration;
    private ResourceLocalizer resourceLocalizer;
    private Map<String, String> environment;
    
    public DefaultContainerLauncher(Configuration configuration, ResourceLocalizer resourceLocalizer, Map<String, String> environment) {
        this.configuration = configuration;
        this.resourceLocalizer = resourceLocalizer;
        this.environment = environment;
    }

    @Override
    public void launchContainer(Container container, List<String> commands) {
        
        AppmasterCmTemplate template = new AppmasterCmTemplate(configuration, container);
        
        try {
            template.afterPropertiesSet();
        } catch (Exception e) {
            log.error("error creating template", e);
        }
        
        ContainerLaunchContext ctx = Records.newRecord(ContainerLaunchContext.class);
        ctx.setContainerId(container.getId());
        ctx.setResource(container.getResource());
        String jobUserName = System.getenv(ApplicationConstants.Environment.USER.name());
        ctx.setUser(jobUserName);
        ctx.setLocalResources(resourceLocalizer.getResources());        
        ctx.setCommands(commands);        
        ctx.setEnvironment(environment);
        
        StartContainerRequest startReq = Records.newRecord(StartContainerRequest.class);
        startReq.setContainerLaunchContext(ctx);
        
        template.startContainer(startReq);
    }

    public void setResourceLocalizer(ResourceLocalizer resourceLocalizer) {
        this.resourceLocalizer = resourceLocalizer;
    }
    
}
