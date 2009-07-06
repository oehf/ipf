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
package org.openehealth.ipf.platform.camel.ihe.xds.iti42;

import static junit.framework.Assert.assertEquals;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.RegistryResponse30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.RegisterDocumentSetTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.responses.ResponseTransformer;

/**
 * Tests the ITI-42 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
public class TestIti42 extends StandardTestContainer {
    private static final String SERVICE1 = "xds-iti42://localhost:9091/xds-iti42-service1";
    private static final String SERVICE2 = "xds-iti42://localhost:9091/xds-iti42-service2";

    private RegisterDocumentSet request;
    private DocumentEntry docEntry;
    
    @BeforeClass
    public static void classSetUp() throws Exception {
        startServer(new CXFServlet(), "iti-42.xml");
        installTestInterceptors(Iti42TestAuditFinalInterceptor.class);
    }

    @Before
    public void setUp() {
        Identifiable patientID = new Identifiable("patient-id", new AssigningAuthority("1.2.3.4.5", "ISO"));
        
        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.setPatientID(patientID);
        submissionSet.setUniqueID("229.6.58.29.24.1235");

        docEntry = new DocumentEntry();
        docEntry.setPatientID(patientID);
        docEntry.setComments(new LocalizedString("service 1"));
        
        request = new RegisterDocumentSet();
        request.setSubmissionSet(submissionSet);
        request.getDocumentEntries().add(docEntry);
    }
        
    /** Calls the route attached to the ITI-42 endpoint. */
    @Test
    public void testIti42() {
        Response response1 = send(SERVICE1, "service 1");
        assertEquals(Status.SUCCESS, response1.getStatus());

        Response response2 = send(SERVICE2, "service 2");
        assertEquals(Status.SUCCESS, response2.getStatus());
    }
    
    private Response send(String endpoint, String value) {
        docEntry.setComments(new LocalizedString(value));
        
        EbXMLFactory30 factory = new EbXMLFactory30();
        RegisterDocumentSetTransformer requestTransformer = new RegisterDocumentSetTransformer(factory);
        SubmitObjectsRequest ebXMLRequest = requestTransformer.toEbXML(request);        
        Object result = getProducerTemplate().requestBody(endpoint, ebXMLRequest.getInternal());        
        RegistryResponse30 ebXMLResponse = RegistryResponse30.create((RegistryResponseType) result);
        ResponseTransformer responseTransformer = new ResponseTransformer(factory);
        return responseTransformer.fromEbXML(ebXMLResponse);
    }
}