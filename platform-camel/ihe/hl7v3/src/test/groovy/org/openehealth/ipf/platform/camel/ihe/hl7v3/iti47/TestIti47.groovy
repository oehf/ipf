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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti47

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils
import org.openehealth.ipf.platform.camel.ihe.mllp.core.EhcacheInteractiveConfigurationStorage
import org.openehealth.ipf.platform.camel.ihe.hl7v3.CustomInterceptor
import org.openehealth.ipf.platform.camel.ihe.hl7v3.EhcacheHl7v3ContinuationStorage
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import org.openehealth.ipf.commons.xml.CombinedXmlValidator
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfiles
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId

/**
 * Tests for ITI-47.
 * @author Dmytro Rud
 */
class TestIti47 extends StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-47.xml'
    
    private final String SERVICE_CONTI =
        "pdqv3-iti47://localhost:${port}/pdqv3-iti47-serviceConti" +
        '?supportContinuation=true' +
        '&autoCancel=true' +
        '&validationOnContinuation=true'
    
    private final String SERVICE_INTERCEPT =
        "pdqv3-iti47://localhost:${port}/pdqv3-iti47-serviceIntercept" +
        '?inInterceptors=#customInterceptorA, #customInterceptorB' +
        '&outInterceptors=#customInterceptorA, #customInterceptorA, #customInterceptorC'

    private final String SERVICE_1 = "pdqv3-iti47://localhost:${port}/pdqv3-iti47-service1"
    private final String SERVICE_NAK_1 = "pdqv3-iti47://localhost:${port}/pdqv3-iti47-serviceNak1"
    private final String SERVICE_NAK_2 = "pdqv3-iti47://localhost:${port}/pdqv3-iti47-serviceNak2"
    private final String SERVICE_NAK_VALIDATE = "pdqv3-iti47://localhost:${port}/pdqv3-iti47-serviceNakValidate"
    
    private final String SERVICE_V2_CONTI = "pdqv3-iti47://localhost:${port}/pdqv3-iti47-serviceV2Conti"
    
    private static final String REQUEST = readFile('iti47/01_PDQQuery1.xml')
    
    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }


    @Test
    void testHappyCaseAndAudit() {
        String responseString = send(SERVICE_1, REQUEST, String.class)
        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            def xml = new XmlSlurper().parseText(it.toString())
            assert xml.EventIdentification.@EventActionCode.text() == 'E'
            assert xml.EventIdentification.@EventOutcomeIndicator.text() == '0'
            assert xml.ParticipantObjectIdentification.size() == 9
        }
    }

    
    @Test
    void testContinuations() {
        String responseString = send(SERVICE_CONTI, REQUEST, String.class)
        
        def response = Hl7v3Utils.slurp(responseString)
        assertEquals('7', response.controlActProcess.queryAck.resultTotalQuantity.@value.text())
        assertEquals('7', response.controlActProcess.queryAck.resultCurrentQuantity.@value.text())
        assertEquals('0', response.controlActProcess.queryAck.resultRemainingQuantity.@value.text())
        
        int subjectCount = 0
        for (subject in response.controlActProcess.subject) {
            ++subjectCount
            assertEquals(subjectCount.toString(), subject.registrationEvent.subject1.patient.id.@extension.text())
        }
        assertEquals(7, subjectCount)
        
        // check whether cancel message has had effect
        EhcacheHl7v3ContinuationStorage storage = appContext.getBean('hl7v3ContinuationStorage')
        assertEquals(0, storage.ehcache.size)

        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            def xml = new XmlSlurper().parseText(it.toString())
            assert xml.EventIdentification.@EventActionCode.text() == 'E'
            assert xml.EventIdentification.@EventOutcomeIndicator.text() == '0'
            assert xml.ParticipantObjectIdentification.size() == 9
        }
    }
    
    
    @Test
    void testCustomInterceptors() {
        String responseString = send(SERVICE_INTERCEPT, '<PRPA_IN201305UV02 xmlns="urn:hl7-org:v3"/>', String.class)
        def response = Hl7v3Utils.slurp(responseString)
        assert response.@from == 'PDSupplier'
        
        assert CustomInterceptor['a'] == 2
        assert CustomInterceptor['b'] == 1
        assert CustomInterceptor['c'] == 1
    }
    
    
    @Test
    void testCustomNakGeneration() {
        String responseString = send(SERVICE_NAK_1, REQUEST, String.class)
        new CombinedXmlValidator().validate(responseString,
                Hl7v3ValidationProfiles.getResponseValidationProfile(IpfInteractionId.ITI_47))
        def response = Hl7v3Utils.slurp(responseString)
        assert response.interactionId.@root == '2.16.840.1.113883.1.6'
        assert response.interactionId.@extension == 'PRPA_IN201306UV02'
        assert response.acknowledgement.typeCode.@code == 'XX'
        assert response.acknowledgement.acknowledgementDetail.code.@code == 'FEHLER'
        assert response.acknowledgement.acknowledgementDetail.text.text() == 'message1'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.code.@code == 'ISSUE'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.mitigatedBy.detectedIssueManagement.code.@code == 'ABCD'
        assert response.controlActProcess.queryAck.statusCode.@code == 'revised'
        assert response.controlActProcess.queryAck.queryResponseCode.@code == 'YY'

        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            assert it.toString().contains('EventOutcomeIndicator="8"')
        }
    }
    
    
    @Test
    void testCustomNakGenerationWithoutIssueManagement() {
        String responseString = send(SERVICE_NAK_2, REQUEST, String.class)
        new CombinedXmlValidator().validate(responseString,
                Hl7v3ValidationProfiles.getResponseValidationProfile(IpfInteractionId.ITI_47))
        def response = Hl7v3Utils.slurp(responseString)
        assert response.interactionId.@root == '2.16.840.1.113883.1.6'
        assert response.interactionId.@extension == 'PRPA_IN201306UV02'
        assert response.acknowledgement.typeCode.@code == 'XX'
        assert response.acknowledgement.acknowledgementDetail.code.@code == 'FEHLER'
        assert response.acknowledgement.acknowledgementDetail.text.text() == 'message1'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.code.@code == 'ISSUE'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.mitigatedBy.size() == 0
        assert response.controlActProcess.queryAck.statusCode.@code == 'revised'
        assert response.controlActProcess.queryAck.queryResponseCode.@code == 'YY'

        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            assert it.toString().contains('EventOutcomeIndicator="8"')
        }
    }
    
    
    @Test
    void testValidationNakGeneration() {
        String responseString = send(SERVICE_NAK_VALIDATE, REQUEST, String.class)
        new CombinedXmlValidator().validate(responseString,
                Hl7v3ValidationProfiles.getResponseValidationProfile(IpfInteractionId.ITI_47))
        def response = Hl7v3Utils.slurp(responseString)
        assert response.acknowledgement.typeCode.@code == 'AE'
        assert response.acknowledgement.acknowledgementDetail.code.@code == 'SYN113'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.code.@code == 'ActAdministrativeDetectedIssueCode'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.mitigatedBy.detectedIssueManagement.code.@code == 'VALIDAT'
        assert response.controlActProcess.queryAck.statusCode.@code == 'aborted'
        assert response.controlActProcess.queryAck.queryResponseCode.@code == 'QE'

        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            assert it.toString().contains('EventOutcomeIndicator="8"')
        }
    }
    
    
    @Test
    void testV2Continuation() {
        String responseString = send(SERVICE_V2_CONTI, REQUEST, String.class)
        
        // check whether the response is full
        def response = Hl7v3Utils.slurp(responseString)
        assertEquals('4', response.controlActProcess.queryAck.resultTotalQuantity.@value.text())
        assertEquals('4', response.controlActProcess.queryAck.resultCurrentQuantity.@value.text())
        assertEquals('0', response.controlActProcess.queryAck.resultRemainingQuantity.@value.text())
        
        // check whether HL7 v2 continuation has really been used
        EhcacheInteractiveConfigurationStorage storage = appContext.getBean('hl7v2ContinuationStorage')
        assertTrue(storage.ehcache.size > 0)
    }
}
