/*
 * Copyright 2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.core.management;

import org.apache.camel.CamelContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Reinhard Luft
 */
public class ProcessorManagementNamingStrategyTest {

    private static final String CONTEXT = "context-core-management.xml";

    private static CamelContext camelContext;

    private static ClassPathXmlApplicationContext appContext;

    @BeforeAll
    public static void setUpContext() {

        appContext = new ClassPathXmlApplicationContext(CONTEXT);
        camelContext = appContext.getBean("camelContext", CamelContext.class);
    }

    @Test
    public void testProcessorManagementNamingStrategy() throws Exception {

        var on = queryForNamedObjects("org.apache.camel:context=camelContext,type=processors,route=\"namingStrategyRoute\",name=\"namingStrategyProcessor\"");

        var oi = getMBeanServer().getObjectInstance(on);
        assertNotNull(oi);
    }

    @AfterAll
    public static void tearDownAfterClass() {

        appContext.close();
    }

    private ObjectName queryForNamedObjects(String query) throws Exception {

        var mbeanServer = getMBeanServer();
        var s = mbeanServer.queryNames(new ObjectName(query), null);
        return (ObjectName) s.toArray()[0];
    }

    private MBeanServer getMBeanServer() {

        return camelContext.getManagementStrategy().getManagementAgent().getMBeanServer();
    }
}
