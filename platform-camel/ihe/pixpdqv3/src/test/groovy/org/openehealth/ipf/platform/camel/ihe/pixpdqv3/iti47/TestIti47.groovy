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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti47

import org.apache.commons.io.IOUtils
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Validator
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.CustomInterceptor
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.EhcacheHl7v3ContinuationStorage
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import static org.junit.Assert.assertEquals

/**
 * Tests for ITI-47.
 * @author Dmytro Rud
 */
class TestIti47 extends StandardTestContainer {

    private final String SERVICE1 =
            "pdqv3-iti47://localhost:${port}/pdqv3-iti47-service1" +
            '?supportContinuation=true' +
            '&autoCancel=true' +
            '&validationOnContinuation=true'

    private final String SERVICE2 =
            "pdqv3-iti47://localhost:${port}/pdqv3-iti47-service2" +
            '?inInterceptors=#customInterceptorA, #customInterceptorB' +
            '&outInterceptors=#customInterceptorA, #customInterceptorA, #customInterceptorC'

    private final String SERVICE3 = "pdqv3-iti47://localhost:${port}/pdqv3-iti47-service3"
    private final String SERVICE4 = "pdqv3-iti47://localhost:${port}/pdqv3-iti47-service4"

    private static final Hl7v3Validator VALIDATOR = new Hl7v3Validator()

    private static final String REQUEST =
            IOUtils.toString(TestIti47.class.classLoader.getResourceAsStream('iti47/01_PDQQuery1.xml'))

    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), 'iti-47.xml')
    }


    @Test
    void testContinuations() {
        String responseString = send(SERVICE1, REQUEST, String.class)

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
        EhcacheHl7v3ContinuationStorage storage = appContext.getBean('continuationStorage')
        assertEquals(0, storage.ehcache.size)
    }


    @Test
    void testCustomInterceptors() {
        String responseString = send(SERVICE2, '<PRPA_IN201305UV02 />', String.class)
        def response = Hl7v3Utils.slurp(responseString)
        assert response.@from == 'PDSupplier'

        assert CustomInterceptor['a'] == 2
        assert CustomInterceptor['b'] == 1
        assert CustomInterceptor['c'] == 1
    }


    @Test
    void testCustomNakGeneration() {
        String responseString = send(SERVICE3, REQUEST, String.class)
        VALIDATOR.validate(responseString, [['PRPA_IN201306UV02', 'iti47/PRPA_IN201306UV02']] as String[][])
        def response = Hl7v3Utils.slurp(responseString)
        assert response.acknowledgement.typeCode.@code == 'XX'
        assert response.acknowledgement.acknowledgementDetail.code.@code == 'FEHLER'
        assert response.acknowledgement.acknowledgementDetail.text.text() == 'message1'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.code.@code == 'ISSUE'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.mitigatedBy.detectedIssueManagement.code.@code == 'ABCD'
        assert response.controlActProcess.queryAck.statusCode.@code == 'revised'
        assert response.controlActProcess.queryAck.queryResponseCode.@code == 'YY'
    }

    @Test
    void testValidationNakGeneration() {
        String responseString = send(SERVICE4, REQUEST, String.class)
        VALIDATOR.validate(responseString, [['PRPA_IN201306UV02', 'iti47/PRPA_IN201306UV02']] as String[][])
        def response = Hl7v3Utils.slurp(responseString)
        assert response.acknowledgement.typeCode.@code == 'AE'
        assert response.acknowledgement.acknowledgementDetail.code.@code == 'SYN113'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.code.@code == 'ActAdministrativeDetectedIssueCode'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.mitigatedBy.detectedIssueManagement.code.@code == 'VALIDAT'
        assert response.controlActProcess.queryAck.statusCode.@code == 'aborted'
        assert response.controlActProcess.queryAck.queryResponseCode.@code == 'QE'
    }

}
