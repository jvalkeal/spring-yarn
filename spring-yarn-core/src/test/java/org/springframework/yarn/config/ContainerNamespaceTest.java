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
package org.springframework.yarn.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("container-ns.xml")
public class ContainerNamespaceTest {

	@Autowired
	private ApplicationContext ctx;

	@Test
	public void testDefaults() {
		assertTrue(ctx.containsBean("yarnContainer"));
		Object bean = ctx.getBean("yarnContainer");
		assertNotNull(bean);
	}

	@Test
	public void testFromReference() {
		assertTrue(ctx.containsBean("yarnContainerFromRef"));
		Object bean = ctx.getBean("yarnContainerFromRef");
		assertNotNull(bean);
	}

	@Test
	public void testFromInner() {
		assertTrue(ctx.containsBean("yarnContainerInner"));
		Object bean = ctx.getBean("yarnContainerInner");
		assertNotNull(bean);
	}

}
