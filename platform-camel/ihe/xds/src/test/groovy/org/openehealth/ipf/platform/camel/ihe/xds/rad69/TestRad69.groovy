/*
 * Copyright 2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")"
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
package org.openehealth.ipf.platform.camel.ihe.xds.rad69

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the RAD-69 transaction with a webservice and client adapter defined via URIs.
 * @author Clay Sebourn
 */
class TestRad69 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'rad-69.xml'
    def SERVICE1 = "xdsi-rad69://localhost:${port}/xdsi-rad69-service1"
    def SERVICE2 = "xdsi-rad69://localhost:${port}/xdsi-rad69-service2"
    def SERVICE2_ADDR = "http://localhost:${port}/xdsi-rad69-service2"
    def request
    def doc
    
    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    public static void classSetUp() throws Exception {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Before
    void setUp() {
        request = SampleData.createRetrieveImagingDocumentSet()
        doc = request.getRetrieveStudies().get(0).getRetrieveSerieses().get(0).getDocuments().get(0)
    }
    
    @Test
    void testRad69() {
        def response1 = sendIt(SERVICE1, 'service 1')
        assert SUCCESS == response1.status
        checkForMTOM(response1)
        
        def response2 = sendIt(SERVICE2, 'service 2')
        assert SUCCESS == response2.status
        checkForMTOM(response2)
        assert auditSender.messages.size() == 4
        
        checkAudit('0', 'service 2')
    }
    
    @Test
    void testRad69FailureAudit() {
        def response2 = sendIt(SERVICE2, 'falsch')
        assert FAILURE == response2.status
        assert auditSender.messages.size() == 2
        
        checkAudit('8', 'falsch')
    }
    
    def checkAudit(outcome, docIdValue) {
        def message = getAudit('R', SERVICE2_ADDR)[0]
        
        assert message.AuditSourceIdentification.size() == 1
        assert message.EventIdentification.size() == 1
        assert message.ActiveParticipant.size() == 2
        assert message.ParticipantObjectIdentification.size() == 8
        assert message.children().size() == 12
        
        checkEvent(message.EventIdentification, '110106', 'RAD-69', 'R', outcome)
        checkSource(message.ActiveParticipant[0], SERVICE2_ADDR, 'false')
        checkDestination(message.ActiveParticipant[1], 'true')
        checkAuditSource(message.AuditSourceIdentification, 'customXdsSourceId')
        checkImageDocument(message.ParticipantObjectIdentification[0], docIdValue, 'urn:oid:1.2.3', 'repo1', 'urn:oid:1.1.1', 'urn:oid:1.2.1')
        checkImageDocument(message.ParticipantObjectIdentification[1], 'doc2', 'urn:oid:1.2.4', 'repo2', 'urn:oid:1.1.1', 'urn:oid:1.2.1')
        
        message = getAudit('C', SERVICE2_ADDR)[0]
        
        assert message.AuditSourceIdentification.size() == 1
        assert message.EventIdentification.size() == 1
        assert message.ActiveParticipant.size() == 2
        assert message.ParticipantObjectIdentification.size() == 8
        assert message.children().size() == 12
        
        checkEvent(message.EventIdentification, '110107', 'RAD-69', 'C', outcome)
        checkSource(message.ActiveParticipant[0], 'false')
        checkDestination(message.ActiveParticipant[1], 'true')
        checkImageDocument(message.ParticipantObjectIdentification[0], docIdValue, 'urn:oid:1.2.3', 'repo1', 'urn:oid:1.1.1', 'urn:oid:1.2.1')
        checkImageDocument(message.ParticipantObjectIdentification[1], 'doc2', 'urn:oid:1.2.4', 'repo2', 'urn:oid:1.1.1', 'urn:oid:1.2.1')
    }
    
    void checkForMTOM(response) {
        def attachments = response.documents[0].dataHandler.dataSource.attachments
        assert attachments.size() == 1
        assert attachments.iterator().next().xop
    }
    
    def sendIt(endpoint, value) {
        doc.documentUniqueId = value
        send(endpoint, request, RetrievedDocumentSet.class)
    }
}
