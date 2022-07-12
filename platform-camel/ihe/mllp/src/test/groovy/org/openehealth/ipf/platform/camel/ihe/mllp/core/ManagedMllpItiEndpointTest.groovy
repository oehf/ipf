/*
 * Copyright 2011 InterComponentWare AG.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core

import org.apache.camel.test.spring.junit5.DisableJmx
import org.junit.jupiter.api.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.core.mbean.SomeMllpItiComponent
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration

import javax.management.MBeanServer
import javax.management.ObjectInstance
import javax.management.ObjectName

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull

@ContextConfiguration('/core/some-mllp-iti-context.xml')
@DisableJmx(false)
class ManagedMllpItiEndpointTest extends AbstractMllpTest {

    @Test
    void initContext() throws Exception {
        ObjectName on = queryForNamedObjects(
                'org.apache.camel:context=camelContext,type=context,name=\"camelContext\"')
        ObjectInstance oi = getMBeanServer().getObjectInstance(on)
        assertNotNull(oi)
    }

    @Test
    void endpointManagedType() throws Exception {
        ObjectName on = queryForNamedObjects(
                'org.apache.camel:*,type=endpoints,name=\"some-mllp-iti:*\"')
        ObjectInstance oi = getMBeanServer().getObjectInstance(on)
        assertEquals(MllpTransactionEndpoint.class.getCanonicalName(), oi.getClassName())
    }

    @Test
    void endpointAttributes() throws Exception {
        ObjectName on = queryForNamedObjects(
                'org.apache.camel:*,type=endpoints,name=\"some-mllp-iti:*\"')
        assertEquals(SomeMllpItiComponent.class.getCanonicalName(),
                (String) getMBeanServer().getAttribute(on, "ComponentType"))
        assertEquals(true,
                ((Boolean) getMBeanServer().getAttribute(on, "Audit")).booleanValue())
        assertEquals(30000L,
                ((Long) getMBeanServer().getAttribute(on, "Timeout")).longValue())
        assertEquals(2,
                ((String[]) getMBeanServer().getAttribute(on, "CustomInterceptorFactoryList")).length)
    }

    /** some helper methods **/
    private ObjectName queryForNamedObjects(String query) {
        MBeanServer mbeanServer = getMBeanServer()
        Set<ObjectName> s = mbeanServer.queryNames(new ObjectName(query), null)
        ObjectName on = (ObjectName) s.toArray()[0]
        on
    }

    private MBeanServer getMBeanServer() {
        def strategy = camelContext.getManagementStrategy()
        return strategy.getManagementAgent().getMBeanServer()
    }
}
