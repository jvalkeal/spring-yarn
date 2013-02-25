package org.springframework.yarn.launch;

/**
 * Implementation of the {@link SystemExiter} interface that calls the standards
 * System.exit method. It should be noted that there will be no unit tests for
 * this class, since there is only one line of actual code, that would only be
 * testable by mocking System or Runtime.
 *
 * @author Janne Valkealahti
 *
 */
public class JvmSystemExiter implements SystemExiter {

	/**
	 * Delegate call to System.exit() with the argument provided. This should only
	 * be used in a scenario where a particular status needs to be returned to
	 * an external scheduler.
	 *
	 * @see org.springframework.yarn.launch.SystemExiter#exit(int)
	 */
	@Override
	public void exit(int status) {
		System.exit(status);
	}

}
