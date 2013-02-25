package org.springframework.yarn.client;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.yarn.fs.ResourceLocalizer;

/**
 * Factory bean building {@link YarnClient} instances.
 * 
 * @author Janne Valkealahti
 *
 */
public class YarnClientFactoryBean implements InitializingBean, FactoryBean<YarnClient> {

    private Map<String, String> environment;
    private List<String> commands;
    //private String queue;
    private ClientRmOperations template;
    private Configuration configuration;
    private ResourceLocalizer resourceLocalizer;
    private String appName = "";
    
    private CommandYarnClient client;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        
        if(template == null) {
            ClientRmTemplate crmt = new ClientRmTemplate(configuration);
            crmt.afterPropertiesSet();
            template = crmt;
        }
        
        client = buildClient();
    }

    @Override
    public YarnClient getObject() throws Exception {
        return client;
    }

    @Override
    public Class<YarnClient> getObjectType() {
        return YarnClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
    
    public void setTemplate(ClientRmOperations template) {
        this.template = template;
    }
    
    public void setResourceLocalizer(ResourceLocalizer resourceLocalizer) {
        this.resourceLocalizer = resourceLocalizer;
    }
    
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
    
    private CommandYarnClient buildClient() {
        CommandYarnClient client = new CommandYarnClient(template);
        client.setCommands(commands);
        
        
        //Circular placeholder reference 'CLASSPATH' in property definitions
//      StringBuilder classPathEnv = new StringBuilder("${CLASSPATH}:./*");
        
//        Map<String, String> env = new HashMap<String, String>();
//        StringBuilder classPathEnv = new StringBuilder("./*");
//        for (String c : configuration.getStrings(YarnConfiguration.YARN_APPLICATION_CLASSPATH,
//                YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH)) {
//            classPathEnv.append(':');
//            classPathEnv.append(c.trim());
//        }
//        classPathEnv.append(":./log4j.properties");
//        env.put("CLASSPATH", classPathEnv.toString());        
//        client.setEnvironment(env);
        
        client.setEnvironment(environment);
        client.setResourceLocalizer(resourceLocalizer);
        client.setAppName(appName);
        return client;
    }
    
}
