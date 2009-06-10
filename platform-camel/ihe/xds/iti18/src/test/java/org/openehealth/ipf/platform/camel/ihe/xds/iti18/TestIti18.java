/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti18;

import static junit.framework.Assert.assertEquals;

import java.util.UUID;

import org.apache.cxf.bus.CXFBusImpl;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestWebContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.AdhocQueryType;

/**
 * Tests the ITI-18 component with the webservice and the client defined within the URI.
 * @author Jens Riemschneider
 */
public class TestIti18 extends StandardTestWebContainer {
    private static final String SERVICE1 = "xds-iti18://localhost:9091/xds-iti18-service1?audit=false";
    private static final String SERVICE2 = "xds-iti18://localhost:9091/xds-iti18-service2";

    @BeforeClass
    public static void setUp() throws Exception {
        startServer(new CXFServlet(), "iti-18.xml");

        /*
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
        */
        
        JaxWsServerFactoryBean jaxwsBean = new JaxWsServerFactoryBean();
        CXFBusImpl bus = (CXFBusImpl)jaxwsBean.getBus();
        bus.getOutInterceptors().add(new Iti18TestAuditFinalInterceptor(true));
        bus.getInInterceptors().add(new Iti18TestAuditFinalInterceptor(false));
    }
    
    /** Calls the route attached to the ITI-18 endpoint. */
    @Test
    public void testIti18() {
        AdhocQueryRequest request = new AdhocQueryRequest();
        request.setAdhocQuery(new AdhocQueryType());
        request.getAdhocQuery().setId(UUID.randomUUID().toString());
        request.setComment("ok");

        AdhocQueryResponse response1 =
                (AdhocQueryResponse) getProducerTemplate().requestBody(SERVICE1, request);

        assertEquals("service 1: ok", response1.getStatus());

        AdhocQueryResponse response2 =
                (AdhocQueryResponse) getProducerTemplate().requestBody(SERVICE2, request);

        assertEquals("service 2: ok", response2.getStatus());
    }
}