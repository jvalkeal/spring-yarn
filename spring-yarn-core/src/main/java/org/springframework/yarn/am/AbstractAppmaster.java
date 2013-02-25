package org.springframework.yarn.am;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.FinishApplicationMasterRequest;
import org.apache.hadoop.yarn.api.protocolrecords.FinishApplicationMasterResponse;
import org.apache.hadoop.yarn.api.protocolrecords.RegisterApplicationMasterResponse;
import org.apache.hadoop.yarn.api.records.ApplicationAttemptId;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.FinalApplicationStatus;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.util.Assert;
import org.springframework.yarn.fs.ResourceLocalizer;
import org.springframework.yarn.support.LifecycleObjectSupport;

/**
 * Base class providing functionality for common application
 * master instances.
 * 
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractAppmaster extends LifecycleObjectSupport {

    private static final Log log = LogFactory.getLog(AbstractAppmaster.class);
    
    /** Environment variables for the process */
    private Map<String, String> environment;
    /** Yarn configuration */
    private Configuration configuration;
    /** Commands for container start */
    private List<String> commands;
    /** Template operations talking to resource manager */
    private AppmasterRmOperations rmTemplate;
    /** Cached app attempt id */
    private ApplicationAttemptId appAttemptId;
    /** Parameters passed to application */
    private Properties parameters;
    /** Resource localizer for the containers */
    private ResourceLocalizer resourceLocalizer;
    
    public AbstractAppmaster() {
    }
    
    /**
     * Global application master instance specific {@link ApplicationAttemptId}
     * is build during this init method.
     * 
     * @see org.springframework.yarn.support.LifecycleObjectSupport#onInit()
     */
    @Override
    protected void onInit() throws Exception {
        super.onInit();
        String amContainerId = environment.get(ApplicationConstants.AM_CONTAINER_ID_ENV);
        Assert.notNull(amContainerId, "AM_CONTAINER_ID env variable has to exist to build appAttemptId");
        ContainerId containerId = ConverterUtils.toContainerId(amContainerId);
        appAttemptId = containerId.getApplicationAttemptId();
    }
    
    @Override
    protected void doStart() {
        registerAppmaster();
    }
    
    @Override
    protected void doStop() {
        finishAppmaster();
    }

    public AppmasterRmOperations getTemplate() {
        return rmTemplate;
    }

    public void setTemplate(AppmasterRmOperations template) {
        this.rmTemplate = template;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }
    
    public Properties getParameters() {
        return parameters;
    }

    public void setParameters(Properties parameters) {
        this.parameters = parameters;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
    
    protected ApplicationAttemptId getApplicationAttemptId() {
        return appAttemptId;
    }
    
    protected RegisterApplicationMasterResponse registerAppmaster() {
        return rmTemplate.registerApplicationMaster(appAttemptId, null, null, null);
    }

    public void setResourceLocalizer(ResourceLocalizer resourceLocalizer) {
        this.resourceLocalizer = resourceLocalizer;
    }

    public ResourceLocalizer getResourceLocalizer() {
        return resourceLocalizer;
    }
    
    protected FinishApplicationMasterResponse finishAppmaster() {
        if(log.isDebugEnabled()) {
            log.debug("Sending finish request to resource manager: appAttemptId=" +
                    appAttemptId + " status=" + FinalApplicationStatus.SUCCEEDED);
        }
        FinishApplicationMasterRequest finishReq = Records.newRecord(FinishApplicationMasterRequest.class);
        finishReq.setAppAttemptId(appAttemptId);
        finishReq.setFinishApplicationStatus(FinalApplicationStatus.SUCCEEDED);
        return rmTemplate.finish(finishReq);        
    }

}
