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
package org.openehealth.ipf.platform.camel.ihe.ws.mbean;

import org.apache.camel.CamelContext;
import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.util.CastUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.util.Set;

/**
 * @author Stefan Ivanov
 */
public class ManagedWsItiEndpointTest extends ContextTestSupport {

    static final String NAME = "org.apache.camel.jmx.mbeanObjectDomainName";
    private static String oldValue;

    @BeforeClass
    public static void setupClass() {
        oldValue = System.getProperty(NAME);
        System.setProperty(NAME, "org.gablorg");
    }

    @AfterClass
    public static void tearDownClass() {
        if (oldValue != null)
            System.setProperty(NAME, oldValue);
        else
            System.clearProperty(NAME);
    }

    @Override
    protected boolean useJmx() {
        return true;
    }

    protected CamelContext createCamelContext() throws Exception {

        CamelContext context = super.createCamelContext();
        context.addComponent("some-ws-iti", new SomeItiComponent());
        return context;
    }


    @Test
    public void testInit() throws Exception {
        MBeanServer mbeanServer = getMBeanServer();
        ObjectName on = ObjectName
                .getInstance("org.gablorg:context=camel-1,type=context,name=\"camel-1\"");
        ObjectInstance oi = mbeanServer.getObjectInstance(on);
        assertNotNull(oi);
    }

    @Test
    public void testEndpointAttributes() throws Exception {
        MBeanServer mbeanServer = getMBeanServer();

        Set<ObjectName> s = CastUtils.cast(mbeanServer.queryNames(new ObjectName(
                "org.gablorg:*,type=endpoints,name=\"some-ws-iti://data*\""), null));
        ObjectName on = (ObjectName) s.toArray()[0];
        assertEquals(SomeItiComponent.WS_CONFIG.isAddressing(),
                mbeanServer.getAttribute(on, "Addressing"));
        assertEquals(SomeItiComponent.WS_CONFIG.isMtom(),
                mbeanServer.getAttribute(on, "Mtom"));
        assertEquals(SomeItiComponent.WS_CONFIG.isSwaOutSupport(),
                mbeanServer.getAttribute(on, "SwaOutSupport"));
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("some-ws-iti:data?audit=false").to("mock:result");
            }
        };
    }

    protected MBeanServer getMBeanServer() {
        return context.getManagementStrategy().getManagementAgent().getMBeanServer();
    }
}
