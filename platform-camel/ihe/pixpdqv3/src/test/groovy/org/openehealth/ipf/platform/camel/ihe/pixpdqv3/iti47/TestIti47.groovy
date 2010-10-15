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
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.CustomInterceptor
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import static org.junit.Assert.assertEquals
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.EhcacheHl7v3ContinuationStorage

/**
 * Tests for ITI-47.
 * @author Dmytro Rud
 */
class TestIti47 extends StandardTestContainer {

    def SERVICE1 = "pdqv3-iti47://localhost:${port}/pdqv3-iti47-service1" +
                   '?supportContinuation=true' +
                   '&autoCancel=true' +
                   '&validationOnContinuation=true'

    def SERVICE2 = "pdqv3-iti47://localhost:${port}/pdqv3-iti47-service2" +
                   '?inInterceptors=#customInterceptorA, #customInterceptorB' +
                   '&outInterceptors=#customInterceptorA, #customInterceptorA, #customInterceptorC'


    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), 'iti-47.xml')
    }


    @Test
    void testContinuations() {
        def request = IOUtils.toString(TestIti47.class.classLoader.getResourceAsStream('iti47/01_PDQQuery1.xml'))
        String responseString = send(SERVICE1, request, String.class)

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
        def response = send(SERVICE2, '<PRPA_IN201305UV02 />', String.class)
        def slurper = new XmlSlurper().parseText(response)
        assert slurper.@from == 'PDSupplier'

        assert CustomInterceptor['a'] == 2
        assert CustomInterceptor['b'] == 1
        assert CustomInterceptor['c'] == 1
    }


}
