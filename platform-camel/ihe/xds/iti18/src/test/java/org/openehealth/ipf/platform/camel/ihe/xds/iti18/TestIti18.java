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

import java.util.Arrays;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.SampleData;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.QueryRegistry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.QueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.SoapVersionTestInterceptor;

/**
 * Tests the ITI-18 component with the webservice and the client defined within the URI.
 * @author Jens Riemschneider
 */
public class TestIti18 extends StandardTestContainer {
    private static final String SERVICE1 = "xds-iti18://localhost:9091/xds-iti18-service1";
    private static final String SERVICE2 = "xds-iti18://localhost:9091/xds-iti18-service2";
    private static final String SAMPLE_SERVICE = "xds-iti18://localhost:9091/myIti18Service";
    
    private static final String SERVICE_SOAPDEFAULT = "xds-iti18://localhost:9091/xds-iti18-service21";
    private static final String SERVICE_SOAP12 = "xds-iti18://localhost:9091/xds-iti18-service22?soap11=false";
    private static final String SERVICE_SOAP11 = "xds-iti18://localhost:9091/xds-iti18-service23?soap11=true";

    private QueryRegistry request;
    private FindDocumentsQuery query;
    
    @BeforeClass
    public static void classSetUp() throws Exception {
        startServer(new CXFServlet(), "iti-18.xml");
        installTestInterceptors(Iti18TestAuditFinalInterceptor.class);
        getCxfBus().getOutInterceptors().add(new SoapVersionTestInterceptor());
    }
    
    @Before
    public void setUp() {
        request = SampleData.createFindDocumentsQuery();
        query = (FindDocumentsQuery) request.getQuery();
    }
    
    /** Calls the route attached to the ITI-18 endpoint. */
    @Test
    public void testIti18() throws Exception {
        assertEquals(Status.SUCCESS, send(SERVICE1, "service 1").getStatus());
        assertEquals(Status.SUCCESS, send(SERVICE2, "service 2").getStatus());
    }

    @Test
    public void testIti18SoapVersions() throws Exception {
        SoapVersionTestInterceptor.setSoapVersion(1.2);
        assertEquals(Status.SUCCESS, send(SERVICE_SOAPDEFAULT, "implicit SOAP 1.2").getStatus());

        SoapVersionTestInterceptor.setSoapVersion(1.1);
        assertEquals(Status.SUCCESS, send(SERVICE_SOAP11, "SOAP 1.1").getStatus());

        SoapVersionTestInterceptor.setSoapVersion(1.2);
        assertEquals(Status.SUCCESS, send(SERVICE_SOAP12, "SOAP 1.2").getStatus());

        SoapVersionTestInterceptor.setSoapVersion(null);
    }
    
    @Test
    public void testSample() {
        QueryResponse response = 
            send(SAMPLE_SERVICE, SampleData.createFindDocumentsQuery(), QueryResponse.class);
        assertEquals(Status.SUCCESS, response.getStatus());
        assertEquals(1, response.getReferences().size());
        assertEquals("document01", response.getReferences().get(0).getId());

        response = 
            send(SAMPLE_SERVICE, SampleData.createGetDocumentsQuery(), QueryResponse.class);
        assertEquals(Status.FAILURE, response.getStatus());
    }

    private QueryResponse send(String endpoint, String value) {
        query.setAuthorPersons(Arrays.asList(value));
        return send(endpoint, request, QueryResponse.class);
    }
}