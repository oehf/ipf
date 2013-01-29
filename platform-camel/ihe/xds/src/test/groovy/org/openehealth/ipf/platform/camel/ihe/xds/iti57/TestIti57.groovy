/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti57

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest
import org.openehealth.ipf.commons.xml.XmlUtils
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import org.openehealth.ipf.platform.camel.ihe.xds.XdsEndpoint

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the ITI-57 transaction with a webservice and client adapter defined via URIs.
 * @author Boris Stanojevic
 */
class TestIti57 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-57.xml'
    
    def SERVICE1 = "xds-iti57://localhost:${port}/xds-iti57-service1"
    def SERVICE2 = "xds-iti57://localhost:${port}/xds-iti57-service2"
    def SERVICE3 = "xds-iti57://localhost:${port}/xds-iti57-service3"

    def SERVICE2_ADDR = "http://localhost:${port}/xds-iti57-service2"
    
    def request
    def docEntry
    
    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Before
    void setUp() {
        request = SampleData.createRegisterDocumentSet()
        docEntry = request.documentEntries[0]
    }
    
    @Test
    void testIti57() {
        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
        assert auditSender.messages.size() == 2
    }
    
    @Test
    void testIti57FailureAudit() {
        assert FAILURE == sendIt(SERVICE1, 'falsch').status
        assert auditSender.messages.size() == 2
        
    }


    def sendIt(endpoint, value) {
        docEntry.comments = new LocalizedString(value)
        return send(endpoint, request, Response.class)
    }
}