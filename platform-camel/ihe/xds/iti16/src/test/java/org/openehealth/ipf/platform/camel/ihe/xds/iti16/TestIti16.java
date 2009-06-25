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
package org.openehealth.ipf.platform.camel.ihe.xds.iti16;

import static junit.framework.Assert.assertEquals;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestWebContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.RegistryResponse;

/**
 * Tests the ITI-16 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
public class TestIti16 extends StandardTestWebContainer {

    private static final String SERVICE1 = "xds-iti16://localhost:9091/xds-iti16-service1";
    private static final String SERVICE2 = "xds-iti16://localhost:9091/xds-iti16-service2";

    @BeforeClass
    public static void setUp() throws Exception {
        startServer(new CXFServlet(), "iti-16.xml");
        installTestInterceptors(Iti16TestAuditFinalInterceptor.class);
    }
    
    /** Calls the route attached to the ITI-16 endpoint. */
    @Test
    public void testIti16() {
        AdhocQueryRequest request = new AdhocQueryRequest();
        request.setSQLQuery("ok");

        RegistryResponse response1 =
                (RegistryResponse)getProducerTemplate().requestBody(SERVICE1, request);

        RegistryObjectType actual1 = 
            (RegistryObjectType) response1.getAdhocQueryResponse().getSQLQueryResult().getObjectRefOrAssociationOrAuditableEvent().get(0);
        assertEquals("service 1: ok", actual1.getObjectType());

        RegistryResponse response2 =
                (RegistryResponse)getProducerTemplate().requestBody(SERVICE2, request);

        RegistryObjectType actual2 = 
            (RegistryObjectType) response2.getAdhocQueryResponse().getSQLQueryResult().getObjectRefOrAssociationOrAuditableEvent().get(0);
        assertEquals("service 2: ok", actual2.getObjectType());
    }
}