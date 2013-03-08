package org.springframework.yarn.integration;

/**
 * Simple echo service.
 * 
 * @author Gary Russell
 *
 */
public class TestService {

	public String test(byte[] bytes) {
		return "echo:" + new String(bytes);
	}

	public String test(String s) {
		return "echo:" + s;
	}

}
