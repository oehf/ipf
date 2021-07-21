/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.modules.cda.CDAR2Parser;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.core.converters.EbXML30Converters;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the Continua HRN transaction.
 * @author Stefan Ivanov
 */
public class ContinuaHrnTransactionTest extends StandardTestContainer {
    
    public static final String CONTEXT_DESCRIPTOR = "continua-hrn-context.xml";

    private static JAXBContext jaxbContext;
    private ProvideAndRegisterDocumentSetRequestType hrnRequest;
    
    public static void main(String... args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }

    @BeforeAll
    public static void setUpClass() throws JAXBException {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR);
        jaxbContext = JAXBContext.newInstance("org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30");
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        initRequest();
    }

    @Test
    public void happyCase() throws Exception {
        var uri = "xds-iti41://localhost:" + getPort() + "/continuaHRNService";
        var response = (Response) send(uri, hrnRequest, Response.class);
        assertEquals(Status.SUCCESS, response.getStatus());
    }


    /**
     * Test whether DOM tree can be used as input for Continua HRN.
     */
    @Test
    public void testDomInputDatatype() throws Exception {
        var uri = "xds-iti41://localhost:" + getPort() + "/continuaHRNService";

        // prepare request, delete original document contents
        var request = EbXML30Converters.convert(hrnRequest);
        assertEquals(1, request.getDocuments().size());
        var xdsDocument = request.getDocuments().get(0);
        xdsDocument.removeContent(DataHandler.class);
        assertEquals(0, xdsDocument.getContentsCount());

        // read in CCD file as DOM tree and make it the new document contents
        var stream = getClass().getClassLoader().getResourceAsStream("continua-hrn/SampleCCDDocument.xml");
        var factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        var builder = factory.newDocumentBuilder();
        var domDocument = builder.parse(stream);
        xdsDocument.setContent(org.w3c.dom.Document.class, domDocument);

        // create data handler from DOM tree
        xdsDocument.getContent(byte[].class);
        xdsDocument.getContent(DataHandler.class);
        assertEquals(3, xdsDocument.getContentsCount());

        // send the resulting request
        var response = (Response) send(uri, hrnRequest, Response.class);
        assertEquals(Status.SUCCESS, response.getStatus());
    }


    /**
     * Test whether MDHT POJO can be used as input for Continua HRN.
     */
    @Test
    public void testMdhtInputDatatype() throws Exception {
        var uri = "xds-iti41://localhost:" + getPort() + "/continuaHRNService";

        // prepare request, delete original document contents
        var request = EbXML30Converters.convert(hrnRequest);
        assertEquals(1, request.getDocuments().size());
        var xdsDocument = request.getDocuments().get(0);
        xdsDocument.removeContent(DataHandler.class);
        assertEquals(0, xdsDocument.getContentsCount());

        // read in CCD file as MDHT and make it the new document contents
        var stream = getClass().getClassLoader().getResourceAsStream("continua-hrn/SampleCCDDocument.xml");
        var mdhtDocument = new CDAR2Parser().parse(stream);
        xdsDocument.setContent(ClinicalDocument.class, mdhtDocument);

        // create data handler from MDHT document
        xdsDocument.getContent(byte[].class);
        xdsDocument.getContent(DataHandler.class);
        assertEquals(3, xdsDocument.getContentsCount());

        // send the resulting request
        var response = (Response) send(uri, hrnRequest, Response.class);
        assertEquals(Status.SUCCESS, response.getStatus());
    }
    
    
    @SuppressWarnings("unchecked")
    private void initRequest() throws Exception {
        var u = jaxbContext.createUnmarshaller();
        var is = getClass().getClassLoader().getResourceAsStream(
            "continua-hrn/ProvideAndRegisterDocumentSet.xml");
        hrnRequest = ((JAXBElement<ProvideAndRegisterDocumentSetRequestType>) u.unmarshal(is)).getValue();
    }
}
