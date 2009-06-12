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
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestWebContainer;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.Ebxml30TestUtils;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.LargeDataSource;
import org.openehealth.ipf.platform.camel.ihe.xds.iti41.service.ProvideAndRegisterDocumentSetRequestType;
import javax.activation.DataHandler;
import javax.activation.DataSource;

/**
 * Tests the ITI-41 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
public class TestIti41 extends StandardTestWebContainer {
    private static final String SERVICE1 = "xds-iti41://localhost:9091/xds-iti41-service1";
    private static final String SERVICE2 = "xds-iti41://localhost:9091/xds-iti41-service2";
    
    @BeforeClass
    public static void setUp() throws Exception {
        startServer(new CXFServlet(), "iti-41.xml");
        installTestInterceptors(Iti41TestAuditFinalInterceptor.class);
    }
    
    /** Calls the route attached to the ITI-41 endpoint. */
    @Test
    public void testIti41() {
        SubmitObjectsRequest submitObjectRequest = Ebxml30TestUtils.createTestSubmitObjectsRequest("ok");
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        request.setSubmitObjectsRequest(submitObjectRequest);

        RegistryResponseType response1 =
                (RegistryResponseType)getProducerTemplate().requestBody(SERVICE1, request);

        assertEquals("service 1: ok", response1.getStatus());

        RegistryResponseType response2 =
                (RegistryResponseType)getProducerTemplate().requestBody(SERVICE2, request);

        assertEquals("service 2: ok", response2.getStatus());
    }

    /** Calls the route attached to the ITI-41 endpoint with a large content stream to
     *  check if the infrastructure is supporting MTOM with efficient memory usage */
    @Test
    public void testIti41LargeDocument() {
        SubmitObjectsRequest submitObjectRequest = Ebxml30TestUtils.createTestSubmitObjectsRequest("large");
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        request.setSubmitObjectsRequest(submitObjectRequest);

        ProvideAndRegisterDocumentSetRequestType.Document document = new ProvideAndRegisterDocumentSetRequestType.Document();
        DataSource dataSource = new LargeDataSource();
        document.setValue(new DataHandler(dataSource));
        request.getDocument().add(document);

        RegistryResponseType response =
            (RegistryResponseType)getProducerTemplate().requestBody(SERVICE1, request);

        assertEquals("service 1: ok", response.getStatus());
    }
}