package org.springframework.yarn.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.springframework.yarn.config.ClientParser;
import org.springframework.yarn.launch.AbstractCommandLineRunner;

/**
 * 
 * java CommandLineClientRunner <contextConfig> <beanName> 
 * java CommandLineClientRunner <contextConfig> <beanName> -submit key1=val1
 * java CommandLineClientRunner <contextConfig> <beanName> -list key1=val1
 * 
 * @author Janne Valkealahti
 *
 */
public class CommandLineClientRunner extends AbstractCommandLineRunner<YarnClient> {

    private static final Log log = LogFactory.getLog(CommandLineClientRunner.class);
    
    private static List<String> validOpts = new ArrayList<String>(1);
    
    static {
        validOpts.add("-list");
    }
    
    public CommandLineClientRunner() {
    }

    @Override
    protected void handleBeanRun(YarnClient bean, String[] parameters, Set<String> opts) {
        if(opts.contains("-list")) {
            print(bean.listApplications());
        } else {
            bean.submitApplication();            
        }
    }

    @Override
    protected String getDefaultBeanIdentifier() {
        return ClientParser.DEFAULT_ID;
    }

    @Override
    protected List<String> getValidOpts() {
        return validOpts;
    }
    
    private void print(List<ApplicationReport> applications) {
        log.info("Listing applications:");
        StringBuilder buf = new StringBuilder();
        for(ApplicationReport a : applications) {
            buf.append(a.getName());
            buf.append('\t');
            buf.append(a.getApplicationId());
            buf.append('\n');
        }
        System.out.println(buf.toString());
    }
    
    public static void main(String[] args) {
        new CommandLineClientRunner().doMain(args);
    }

}
