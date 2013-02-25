package org.springframework.yarn.launch;

/**
 * Interface for exiting the JVM. This abstraction is only useful in order to
 * allow classes that make System.exit calls to be testable, since calling
 * System.exit during a unit test would cause the entire jvm to finish.
 * 
 * @author Janne Valkealahti
 * 
 */
public interface SystemExiter {

    /**
     * Terminate the currently running Java Virtual Machine.
     * 
     * @param status
     *            exit status.
     * @throws SecurityException
     *             if a security manager exists and its <code>checkExit</code>
     *             method doesn't allow exit with the specified status.
     * @see System#exit(int)
     */
    void exit(int status);
}
