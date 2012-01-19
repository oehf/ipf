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

import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.management.DefaultManagementNamingStrategy;
import org.apache.camel.management.ManagementTestSupport;
import org.apache.camel.util.CastUtils;

/**
 * 
 * @author Stefan Ivanov
 * 
 */
public class ManagedWsItiEndpointTest extends ManagementTestSupport {
    
    @Override
    protected boolean useJmx() {
        return true;
    }
    
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.addComponent("some-ws-iti", new SomeItiComponent());
        DefaultManagementNamingStrategy naming = (DefaultManagementNamingStrategy) context
            .getManagementStrategy().getManagementNamingStrategy();
        naming.setHostName("localhost");
        naming.setDomainName("org.apache.camel");
        return context;
    }
    
    public void testInit() throws Exception {
        MBeanServer mbeanServer = getMBeanServer();
        ObjectName on = ObjectName
            .getInstance("org.apache.camel:context=localhost/camel-1,type=context,name=\"camel-1\"");
        ObjectInstance oi = mbeanServer.getObjectInstance(on);
        assertNotNull(oi);
    }


    public void testEndpointAttributes() throws Exception {
        MBeanServer mbeanServer = getMBeanServer();
        
        Set<ObjectName> s = CastUtils.cast(mbeanServer.queryNames(new ObjectName(
            "org.apache.camel:*,type=endpoints,name=\"some-ws-iti://data\""), null));
        ObjectName on = (ObjectName) s.toArray()[0];
        assertEquals(SomeItiComponent.WS_CONFIG.isAddressing(),
            ((Boolean) mbeanServer.getAttribute(on, "Addressing")).booleanValue());
        assertEquals(SomeItiComponent.WS_CONFIG.isMtom(),
            ((Boolean) mbeanServer.getAttribute(on, "Mtom")).booleanValue());
        assertEquals(SomeItiComponent.WS_CONFIG.isSwaOutSupport(),
            ((Boolean) mbeanServer.getAttribute(on, "SwaOutSupport")).booleanValue());
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("some-ws-iti:data").to("mock:result");
            }
        };
    }

}
