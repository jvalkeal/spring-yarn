/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.yarn.integration.convert;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.yarn.integration.ip.mind.MindRpcMessageHolder;
import org.springframework.yarn.integration.ip.mind.SimpleTestRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests for {@link MindHolderToObjectConverter} and
 * {@link MindObjectToHolderConverter}.
 *
 * @author Janne Valkealahti
 *
 */
public class MindConverterTests {

	private MindHolderToObjectConverter holderToObject;
	private MindObjectToHolderConverter objectToHolder;

	@Before
	public void setUp() {
		ObjectMapper objectMapper = new ObjectMapper();
		holderToObject = new MindHolderToObjectConverter(objectMapper, "org.springframework.yarn.integration.ip.mind");
		objectToHolder = new MindObjectToHolderConverter(objectMapper);
	}

	@Test
	public void testSimpleConversion() {
		SimpleTestRequest request1 = new SimpleTestRequest();
		MindRpcMessageHolder holder = objectToHolder.convert(request1);
		assertThat(holder, notNullValue());
		SimpleTestRequest request2 = (SimpleTestRequest) holderToObject.convert(holder);
		assertThat(request2, notNullValue());
		assertThat(request1.type, is(request2.type));
		assertThat(request1.stringField, is(request2.stringField));
	}

}
