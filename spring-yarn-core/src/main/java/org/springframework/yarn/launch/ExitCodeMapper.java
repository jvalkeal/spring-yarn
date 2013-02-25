package org.springframework.yarn.launch;

/**
 * This interface should be implemented when an environment calling the batch
 * framework has specific requirements regarding the operating system process
 * return status.
 * 
 * @author Janne Valkealahti
 * 
 */
public interface ExitCodeMapper {

    public static int JVM_EXITCODE_COMPLETED = 0;

    public static int JVM_EXITCODE_GENERIC_ERROR = 1;

    // we keep below codes here as a reservation
    // because these exist in yarn
    public static int JVM_EXITCODE_INVALID_USER_NAME = 2;
    public static int JVM_EXITCODE_UNABLE_TO_EXECUTE_CONTAINER_SCRIPT = 7;
    public static int JVM_EXITCODE_INVALID_CONTAINER_PID = 9;
    public static int JVM_EXITCODE_INVALID_CONTAINER_EXEC_PERMISSIONS = 22;
    public static int JVM_EXITCODE_INVALID_CONFIG_FILE = 24;
    public static int JVM_EXITCODE_WRITE_CGROUP_FAILED = 27;    
    public static int JVM_EXITCODE_FORCE_KILLED = 137;    
    public static int JVM_EXITCODE_TERMINATED = 143;    

    /**
     * Convert the exit code from String into an integer that the calling
     * environment as an operating system can interpret as an exit status.
     * 
     * @param exitCode The exit code which is used internally.
     * @return The corresponding exit status as known by the calling
     *         environment.
     */
    public int intValue(String exitCode);

}
