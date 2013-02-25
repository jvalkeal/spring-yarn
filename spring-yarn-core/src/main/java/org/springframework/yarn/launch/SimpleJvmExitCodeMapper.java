package org.springframework.yarn.launch;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An implementation of {@link ExitCodeMapper} that can be configured through a
 * map from batch exit codes (String) to integer results. Some default entries
 * are set up to recognise common cases. Any that are injected are added to
 * these.
 * 
 * @author Janne Valkealahti
 * 
 */
public class SimpleJvmExitCodeMapper implements ExitCodeMapper {

    protected static final Log log = LogFactory.getLog(SimpleJvmExitCodeMapper.class);

    private Map<String, Integer> mapping;

    public SimpleJvmExitCodeMapper() {
        mapping = new HashMap<String, Integer>();
        mapping.put(ExitStatus.COMPLETED.getExitCode(), JVM_EXITCODE_COMPLETED);
        mapping.put(ExitStatus.FAILED.getExitCode(), JVM_EXITCODE_GENERIC_ERROR);
        // TODO: possibly add yarn related mappings
    }

    public Map<String, Integer> getMapping() {
        return mapping;
    }

    /**
     * Supply the ExitCodeMappings
     * 
     * @param exitCodeMap
     *            A set of mappings between environment specific exit codes and
     *            batch framework internal exit codes
     */
    public void setMapping(Map<String, Integer> exitCodeMap) {
        mapping.putAll(exitCodeMap);
    }

    /**
     * Get the operating system exit status that matches a certain Batch
     * Framework Exitcode
     * 
     * @param exitCode
     *            The exitcode of the Batch Job as known by the Batch Framework
     * @return The exitCode of the Batch Job as known by the JVM
     */
    @Override
    public int intValue(String exitCode) {

        Integer statusCode = null;

        try {
            statusCode = mapping.get(exitCode);
        } catch (RuntimeException ex) {
            // We still need to return an exit code, even if there is an issue
            // with the mapper.
            log.fatal("Error mapping exit code, generic exit status returned.", ex);
        }

        return (statusCode != null) ? statusCode.intValue() : JVM_EXITCODE_GENERIC_ERROR;
    }

}
