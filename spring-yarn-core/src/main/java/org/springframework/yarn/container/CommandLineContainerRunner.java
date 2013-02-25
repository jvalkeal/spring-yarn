package org.springframework.yarn.container;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.yarn.launch.AbstractCommandLineRunner;

/**
 * 
 * 
 * @author Janne Valkealahti
 *
 */
public class CommandLineContainerRunner extends AbstractCommandLineRunner<YarnContainer> {

    private static final Log log = LogFactory.getLog(CommandLineContainerRunner.class);

    @Override
    protected void handleBeanRun(YarnContainer bean, String[] parameters, Set<String> opts) {
        log.debug("Starting YarnClient bean: " + StringUtils.arrayToCommaDelimitedString(parameters));
        bean.run();
    }

    @Override
    protected String getDefaultBeanIdentifier() {
        return "yarnContainer";
    }

    @Override
    protected List<String> getValidOpts() {
        return null;
    }

    public static void main(String[] args) {
        new CommandLineContainerRunner().doMain(args);
    }

}
