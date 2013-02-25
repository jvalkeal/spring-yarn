package org.springframework.yarn.am;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.yarn.config.MasterParser;
import org.springframework.yarn.launch.AbstractCommandLineRunner;

/**
 * 
 * 
 * @author Janne Valkealahti
 *
 */
public class CommandLineAppmasterRunner extends AbstractCommandLineRunner<YarnAppmaster>{

    private static final Log log = LogFactory.getLog(CommandLineAppmasterRunner.class);

    @Override
    protected void handleBeanRun(YarnAppmaster bean, String[] parameters, Set<String> opts) {
        Properties properties = StringUtils.splitArrayElementsIntoProperties(parameters, "=");
        bean.setParameters(properties != null ? properties : new Properties());
        log.debug("Starting YarnAppmaster bean: " + StringUtils.arrayToCommaDelimitedString(parameters));
        bean.submitApplication();
        log.debug("Waiting YarnAppmaster bean");
        bean.waitForCompletion();
        log.debug("Waiting YarnAppmaster bean done");
    }

    @Override
    protected String getDefaultBeanIdentifier() {
        return MasterParser.DEFAULT_ID;
    }

    @Override
    protected List<String> getValidOpts() {
        return null;
    }

    public static void main(String[] args) {
        new CommandLineAppmasterRunner().doMain(args);
    }

}
