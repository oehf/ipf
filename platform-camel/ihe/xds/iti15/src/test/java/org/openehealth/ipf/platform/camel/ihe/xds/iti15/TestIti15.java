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
package org.openehealth.ipf.platform.camel.ihe.xds.iti15;

import static junit.framework.Assert.assertEquals;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestWebContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.Ebxml21TestUtils;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.LargeDataSource;
import org.openehealth.ipf.platform.camel.ihe.xds.iti15.service.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.platform.camel.ihe.xds.iti15.service.ProvideAndRegisterDocumentSetRequestType.Document;

/**
 * Tests the ITI-15 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
public class TestIti15 extends StandardTestWebContainer {
    private static final String SERVICE1 = "xds-iti15://localhost:9091/xds-iti15-service1";
    private static final String SERVICE2 = "xds-iti15://localhost:9091/xds-iti15-service2";
    
    @BeforeClass
    public static void setUp() throws Exception {
        startServer(new CXFServlet(), "iti-15.xml");
        installTestInterceptors(Iti15TestAuditFinalInterceptor.class);
    }
    
    /** Calls the route attached to the ITI-15 endpoint. */
    @Test
    public void testIti15() {
        SubmitObjectsRequest submitObjectsRequest = Ebxml21TestUtils.createTestSubmitObjectRequest();
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        request.setSubmitObjectsRequest(submitObjectsRequest);
        
        Document document = new Document();
        DataSource dataSource = new LargeDataSource();
        document.setId("testdoc");
        document.setValue(new DataHandler(dataSource));
        request.getDocument().add(document);
        
        RegistryResponse response1 =
                (RegistryResponse)getProducerTemplate().requestBody(SERVICE1, request);

        assertEquals("service 1: ok", response1.getStatus());

        RegistryResponse response2 =
                (RegistryResponse)getProducerTemplate().requestBody(SERVICE2, request);

        assertEquals("service 2: ok", response2.getStatus());
    }
}