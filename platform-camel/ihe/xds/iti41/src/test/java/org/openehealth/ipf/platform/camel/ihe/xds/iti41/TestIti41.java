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
package org.openehealth.ipf.platform.camel.ihe.xds.iti41;

import static junit.framework.Assert.assertEquals;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.SampleData;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestContainer;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;

/**
 * Tests the ITI-41 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
public class TestIti41 extends StandardTestContainer {
    private final String SERVICE1 = "xds-iti41://localhost:" + getPort() + "/xds-iti41-service1";
    private final String SERVICE2 = "xds-iti41://localhost:" + getPort() + "/xds-iti41-service2";
    
    private ProvideAndRegisterDocumentSet request;
    private DocumentEntry docEntry;

    @BeforeClass
    public static void classSetUp() throws Exception {
        startServer(new CXFServlet(), "iti-41.xml");
        installTestInterceptors(Iti41TestAuditFinalInterceptor.class);
    }
    
    @Before
    public void setUp() {
        request = SampleData.createProvideAndRegisterDocumentSet();
        docEntry = request.getDocuments().get(0).getDocumentEntry();
    }
    
    /** Calls the route attached to the ITI-41 endpoint. */
    @Test
    public void testIti41() {
        Response response1 = send(SERVICE1, "service 1");
        assertEquals(Status.SUCCESS, response1.getStatus());

        Response response2 = send(SERVICE2, "service 2");
        assertEquals(Status.SUCCESS, response2.getStatus());
    }

    private Response send(String endpoint, String value) {
        docEntry.setComments(new LocalizedString(value));
        return send(endpoint, request, Response.class);
    }
}