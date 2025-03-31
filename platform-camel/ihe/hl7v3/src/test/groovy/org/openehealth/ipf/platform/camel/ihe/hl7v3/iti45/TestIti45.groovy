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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti45

import groovy.xml.XmlSlurper
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils
import org.openehealth.ipf.commons.ihe.hl7v3.PIXV3
import org.openehealth.ipf.commons.xml.CombinedXmlValidator
import org.openehealth.ipf.platform.camel.ihe.hl7v3.HL7v3StandardTestContainer

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.stream.StreamSource

/**
 * Tests for ITI-45.
 * @author Dmytro Rud
 */
class TestIti45 extends HL7v3StandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-45.xml'
    
    def SERVICE1 = "pixv3-iti45://localhost:${port}/pixv3-iti45-service1" 
    def SERVICE2 = "pixv3-iti45://localhost:${port}/pixv3-iti45-service2?audit=false" 
    def SERVICE_NAK1 = "pixv3-iti45://localhost:${port}/pixv3-iti45-serviceNak1?audit=false"

    private static final String REQUEST =
        readFile('translation/pixquery/v3/NistPixpdq_Mesa10501-04_Example_01.xml')

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }
    
    @BeforeAll
    static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Test
    void testIti45() {
        def response = send(SERVICE1, REQUEST, String.class)
        assert auditSender.messages.size() == 2
        auditSender.messages.each {
            assert it.eventIdentification.eventActionCode == EventActionCode.Execute
            assert it.eventIdentification.eventOutcomeIndicator == EventOutcomeIndicator.Success
        }
    }


    @Test
    void testIti45XmlProcessing() {
        def response = send(SERVICE2, '<PRPA_IN201309UV02 xmlns="urn:hl7-org:v3"/>', String.class)
        def slurper = new XmlSlurper().parseText(response)
        assert slurper.@from == 'PIX Manager'
    }


    @Test
    void testDatatypes() {
        String request = '<PRPA_IN201309UV02 xmlns="urn:hl7-org:v3"/>'

        // String
        send(SERVICE2, request, String.class)

        // byte[]
        send(SERVICE2, request.bytes, String.class)

        // Stream
        send(SERVICE2, new ByteArrayInputStream(request.bytes), String.class)

        // Reader
        send(SERVICE2, new InputStreamReader(new ByteArrayInputStream(request.bytes)), String.class)

        // DOM Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance()
        factory.setNamespaceAware(true)
        DocumentBuilder builder = factory.newDocumentBuilder()
        send(SERVICE2, builder.parse(new ByteArrayInputStream(request.getBytes("UTF-8"))))

        // Source
        send(SERVICE2, new StreamSource(new ByteArrayInputStream(request.bytes)), String.class)

        // Unsupported type
        boolean caught = false
        try {
            send(SERVICE2, 666, String.class)
        } catch (Exception e) {
            caught = true
        }
        assert caught
    }


    @Test
    void testCustomNakGeneration() {
        String responseString = send(SERVICE_NAK1, REQUEST, String.class)
        new CombinedXmlValidator().validate(responseString, PIXV3.Interactions.ITI_45.responseValidationProfile)
        def response = Hl7v3Utils.slurp(responseString)
        assert response.acknowledgement.acknowledgementDetail.@typeCode == 'E'
        assert response.acknowledgement.acknowledgementDetail.code.@code == '204'
        assert response.acknowledgement.acknowledgementDetail.code.@codeSystem == '2.16.840.1.113883.18.217'
        assert response.acknowledgement.acknowledgementDetail.text.text() == 'ERROR'
        assert response.acknowledgement.acknowledgementDetail.location.text() == '/PRPA_IN201309UV02/controlActProcess/queryByParameter/parameterList/patientIdentifier[1]'
        assert response.controlActProcess.queryAck.queryResponseCode.@code == 'AE'
    }
}
