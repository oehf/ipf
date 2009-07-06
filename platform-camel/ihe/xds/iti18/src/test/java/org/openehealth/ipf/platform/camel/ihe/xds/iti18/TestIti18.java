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

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.QueryRegistry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.QueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;

/**
 * Tests the ITI-18 component with the webservice and the client defined within the URI.
 * @author Jens Riemschneider
 */
public class TestIti18 extends StandardTestContainer {
    private static final String SERVICE1 = "xds-iti18://localhost:9091/xds-iti18-service1?audit=false";
    private static final String SERVICE2 = "xds-iti18://localhost:9091/xds-iti18-service2";

    private QueryRegistry request;
    private FindDocumentsQuery query;
    
    @BeforeClass
    public static void classSetUp() throws Exception {
        startServer(new CXFServlet(), "iti-18.xml");
        installTestInterceptors(Iti18TestAuditFinalInterceptor.class);
    }
    
    @Before
    public void setUp() {
        query = new FindDocumentsQuery();
        request = new QueryRegistry(query);        
    }
    
    /** Calls the route attached to the ITI-18 endpoint. */
    @Test
    public void testIti18() {
        assertEquals(Status.SUCCESS, send(SERVICE1, "service 1").getStatus());
        assertEquals(Status.SUCCESS, send(SERVICE2, "service 2").getStatus());
    }

    private QueryResponse send(String endpoint, String value) {
        query.getAuthorPersons().clear();
        query.getAuthorPersons().add(value);
        return send(endpoint, request, QueryResponse.class);
    }
}