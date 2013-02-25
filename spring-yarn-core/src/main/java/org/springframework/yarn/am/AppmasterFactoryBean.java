package org.springframework.yarn.am;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * Factory bean building {@link YarnAppmaster} instances.
 * 
 * @author Janne Valkealahti
 *
 */
public class AppmasterFactoryBean implements InitializingBean, FactoryBean<YarnAppmaster> {

    private Map<String, String> environment;
    private List<String> commands;
    private Configuration configuration;
    private YarnAppmaster master;
    private AppmasterRmOperations template;
    private ResourceLocalizer resourceLocalizer;
    
    @Override
    public YarnAppmaster getObject() throws Exception {
        return master;
    }

    @Override
    public Class<YarnAppmaster> getObjectType() {
        return YarnAppmaster.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        
        if(template == null) {
            AppmasterRmTemplate armt = new AppmasterRmTemplate(configuration);
            armt.afterPropertiesSet();
            template = armt;
        }
        
        StaticAppmaster statMaster = new StaticAppmaster();
        statMaster.setConfiguration(configuration);
        statMaster.setCommands(commands);
        statMaster.setTemplate(template);
        statMaster.setEnvironment(environment);
        statMaster.setResourceLocalizer(resourceLocalizer);
        statMaster.afterPropertiesSet();
        master = statMaster;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    
    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
    
    public void setTemplate(AppmasterRmOperations template) {
        this.template = template;
    }

    public void setResourceLocalizer(ResourceLocalizer resourceLocalizer) {
        this.resourceLocalizer = resourceLocalizer;
    }

}
