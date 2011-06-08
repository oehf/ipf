package org.openehealth.ipf.platform.camel.ihe.mllp.core;


import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

import java.util.Set

import javax.management.MBeanServer
import javax.management.ObjectInstance
import javax.management.ObjectName

import org.apache.camel.CamelContext
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.core.mbean.ManagedMllpItiEndpoint
import org.openehealth.ipf.platform.camel.ihe.mllp.core.mbean.SomeMllpItiComponent

public class ManagedMllpItiEndpointTest extends MllpTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'some-mllp-iti-context.xml'
    
    static void main(args) {
        init(CONTEXT_DESCRIPTOR)
    }
    
    @BeforeClass
    static void setUpClass() {
        init(CONTEXT_DESCRIPTOR)
    }
    
    @Test
    void initContext() throws Exception {
        ObjectName on = queryForNamedObjects(
                'org.apache.camel:context=*/camelContext,type=context,name=\"camelContext\"')
        ObjectInstance oi = getMBeanServer().getObjectInstance(on)
        assertNotNull(oi)
    }
    
    @Test
    void endpointManagedType() throws Exception {
        ObjectName on = queryForNamedObjects(
                'org.apache.camel:*,type=endpoints,name=\"mina:tcp:*\"')
        ObjectInstance oi = getMBeanServer().getObjectInstance(on)
        assertEquals(ManagedMllpItiEndpoint.class.getCanonicalName(), oi.getClassName());
    }
    
    @Test
    void endpointAttributes() throws Exception {
        ObjectName on = queryForNamedObjects(
                'org.apache.camel:*,type=endpoints,name=\"mina:tcp:*\"')
        assertEquals(SomeMllpItiComponent.class.getCanonicalName(),
                (String) getMBeanServer().getAttribute(on, "ComponentType"));
        assertEquals(true,
                ((Boolean) getMBeanServer().getAttribute(on, "Audit")).booleanValue());
        assertEquals(30000L,
                ((Long) getMBeanServer().getAttribute(on, "Timeout")).longValue());
        assertEquals(true,
                ((Boolean) getMBeanServer().getAttribute(on, "SslSecure")).booleanValue());
        assertEquals(2,
                ((String[]) getMBeanServer().getAttribute(on, "CustomInterceptors")).length);
    }
    
    /** some helper getters **/
    private ObjectName queryForNamedObjects(String query) {
        MBeanServer mbeanServer = getMBeanServer()
        Set<ObjectName> s = mbeanServer.queryNames(new ObjectName(query), null)
        ObjectName on = (ObjectName) s.toArray()[0]
        on
    }
    
    private CamelContext getContext() {
        return (CamelContext) super.getCamelContext();
    }
    
    private MBeanServer getMBeanServer() {
        return getContext().getManagementStrategy().getManagementAgent().getMBeanServer();
    }
}
