package org.springframework.yarn.configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * FactoryBean for creating a Map of environment variables.
 * 
 * @author Janne Valkealahti
 * 
 */
public class EnvironmentFactoryBean implements InitializingBean, FactoryBean<Map<String, String>> {

    /** Returned map will be build into this */
    private Map<String, String> environment;
    
    /** Incoming properties for environment */
    private Properties properties;
    
    /** Incoming classpath defined externally, i.e. nested properties. */
    private String classpath;
    
    /** Flag indicating if system env properties should be included */
    private boolean includeSystemEnv = true;
    
    /**
     * Flag indicating if a default yarn entries should be added
     * to a classpath. Effectively entries will be resolved from
     * {@link YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH}.
     */
    private boolean defaultYarnAppClasspath;
    
    /** Delimiter used in a classpath string */
    private String delimiter;
    
    @Override
    public Map<String, String> getObject() throws Exception {
        return environment;
    }

    @Override
    public Class<?> getObjectType() {
        return (environment != null ? environment.getClass() : Map.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        environment = createEnvironment();
        
        if(properties != null) {
            // if we have properties, merge those into environment
            CollectionUtils.mergePropertiesIntoMap(properties, environment);            
        }

        boolean addDelimiter = false;
        
        // set CLASSPATH variable if there's something to set
        StringBuilder classPathEnv = new StringBuilder();
        if(StringUtils.hasText(classpath)) {
            classPathEnv.append(classpath);
            addDelimiter = true;
        }

        if(defaultYarnAppClasspath) {            
            Iterator<String> iterator = Arrays.asList(YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH).iterator();

            // add delimiter if we're about to add something
            if(iterator.hasNext()) {
                classPathEnv.append(addDelimiter ? delimiter : "");                
            }
            
            while(iterator.hasNext()) {
                classPathEnv.append(iterator.next());
                if(iterator.hasNext()) {
                    classPathEnv.append(delimiter);                    
                }
            }            
        }
        
        String classpathString = classPathEnv.toString();
        if(StringUtils.hasText(classpathString)) {
            environment.put("CLASSPATH", classpathString);                    
        }
    }

    /**
     * If set to true properties from a {@link System#getenv()} will
     * be included to environment settings. Default value is true.
     * 
     * @param includeSystemEnv flag to set
     */
    public void setIncludeSystemEnv(boolean includeSystemEnv) {
        this.includeSystemEnv = includeSystemEnv;
    }

    /**
     * Creates the {@link Map} to be returned from this factory bean.
     * 
     * @return map of environment variables
     */
    protected Map<String, String> createEnvironment() {
        if(includeSystemEnv) {
            return new HashMap<String, String>(System.getenv());
        } else {
            return new HashMap<String, String>();            
        }
    }
    
    /**
     * Sets the configuration properties.
     * 
     * @param properties The properties to set.
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Sets incoming classpath.
     * 
     * @param classpath the incoming classpath to set
     */
    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    /**
     * If set to true a default 'yarn' entries will be added to
     * a 'CLASSPATH' environment variable.
     * 
     * @param defaultYarnAppClasspath Flag telling if default yarn entries
     *                                should be added to classpath
     */
    public void setDefaultYarnAppClasspath(boolean defaultYarnAppClasspath) {
        this.defaultYarnAppClasspath = defaultYarnAppClasspath;
    }

    /**
     * Sets the delimiter used in a classpath.
     * 
     * @param delimiter delimiter to use in classpath
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

}
