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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws.pcd01;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.camel.util.CastUtils;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

/**
 * @author Stefan Ivanov
 * 
 */
public class Pcd01ManagedEndpointTest extends StandardTestContainer {
    
    @BeforeClass
    public static void setUpClass() {
        startServer(new CXFServlet(), "pcd-01.xml");
    }
    
    @Test
    public void jmxAttribute() throws Exception {
        MBeanServer mbsc = getCamelContext().getManagementStrategy().getManagementAgent()
            .getMBeanServer();
        Set<ObjectName> s = CastUtils.cast(mbsc.queryNames(new ObjectName(
            "org.apache.camel:*,type=endpoints,name=\"pcd-pcd01://devicedata\""), null));
        assertEquals(1, s.size());
        ObjectName object = (ObjectName) s.toArray()[0];
        assertNotNull(object);
        assertTrue((Boolean) mbsc.getAttribute(object, "Addressing"));
    }
    
}
