package org.springframework.yarn.config;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.apache.hadoop.conf.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Namespace tests for yarn:configuration elements.
 * 
 * @author Janne Valkealahti
 * 
 */
@ContextConfiguration("/org/springframework/yarn/config/configuration-ns.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ConfigurationNamespaceTest {

    @Resource(name = "yarnConfiguration")
    private Configuration defaultConfig;

    @Resource
    private Configuration complexConfig;

    @Resource
    private Configuration propsConfig;

    @Test
    public void testDefaultConfiguration() throws Exception {
        assertNotNull(defaultConfig);
        assertEquals("jee", defaultConfig.get("test.foo"));
        assertNull(defaultConfig.get("test.foo.2"));
        assertNull(defaultConfig.get("resource.property"));
        assertNull(defaultConfig.get("resource.property.2"));
        assertEquals("10.10.10.10:8032", defaultConfig.get("yarn.resourcemanager.address"));
    }

    @Test
    public void testComplexConfiguration() throws Exception {
        assertNotNull(complexConfig);
        assertEquals("jee", complexConfig.get("test.foo"));
        assertEquals("10.10.10.10:8032", complexConfig.get("yarn.resourcemanager.address"));
        assertEquals("test-site-1.xml", complexConfig.get("resource.property"));
        assertEquals("test-site-2.xml", complexConfig.get("resource.property.2"));
    }

    @Test
    public void testPropertiesConfiguration() throws Exception {
        assertNotNull(propsConfig);
        assertEquals("jee20", propsConfig.get("foo20"));
        assertEquals("jee21", propsConfig.get("foo21"));
        assertEquals("jee22", propsConfig.get("foo22"));
        assertEquals("jee23", propsConfig.get("foo23"));
        assertEquals("jee24", propsConfig.get("foo24"));
        assertEquals("jee25", propsConfig.get("foo25"));
        assertEquals("jee26", propsConfig.get("foo26"));
        assertEquals("jee27", propsConfig.get("foo27"));
        assertEquals("jee28", propsConfig.get("foo28"));
    }

}
