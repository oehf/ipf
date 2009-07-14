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
package org.openehealth.ipf.platform.camel.ihe.xds.iti43;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.apache.cxf.transport.servlet.CXFServlet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.SampleData;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.CxfTestUtils;

/**
 * Tests the ITI-43 transaction with a webservice and client adapter defined via URIs.
 * author Jens Riemschneider
 */
public class TestIti43 extends StandardTestContainer {
    private static final String SERVICE1 = "xds-iti43://localhost:9091/xds-iti43-service1";
    private static final String SERVICE2 = "xds-iti43://localhost:9091/xds-iti43-service2";

    private RetrieveDocumentSet request;
    private RetrieveDocument doc;
    
    @BeforeClass
    public static void classSetUp() throws Exception {
        startServer(new CXFServlet(), "iti-43.xml");
        installTestInterceptors(Iti43TestAuditFinalInterceptor.class);        
    }

    @Before
    public void setUp() {
        request = SampleData.createRetrieveDocumentSet();
        doc = request.getDocuments().get(0);
    }
    
    /** Calls the route attached to the ITI-43 endpoint. */
    @Test
    public void testIti43() throws Exception {
        RetrievedDocumentSet response1 = send(SERVICE1, "service 1");
        assertEquals(Status.SUCCESS, response1.getStatus());
        checkForMTOM(response1);
        
        RetrievedDocumentSet response2 = send(SERVICE2, "service 2");
        assertEquals(Status.SUCCESS, response2.getStatus());
        checkForMTOM(response2);
    }

    private void checkForMTOM(RetrievedDocumentSet response1) throws IOException {
        InputStream inputStream = response1.getDocuments().get(0).getDataHandler().getInputStream();
        try {
            assertTrue(CxfTestUtils.isCxfUsingMtom(inputStream));
        }
        finally {
            inputStream.close();
        }
    }

    private RetrievedDocumentSet send(String endpoint, String value) {
        doc.setDocumentUniqueID(value);
        return send(endpoint, request, RetrievedDocumentSet.class);
    }
}