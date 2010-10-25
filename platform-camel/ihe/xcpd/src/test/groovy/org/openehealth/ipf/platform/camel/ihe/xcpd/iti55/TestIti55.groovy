/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xcpd.iti55;

import java.util.concurrent.atomic.AtomicInteger
import org.apache.camel.impl.DefaultExchange
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import org.openehealth.ipf.platform.camel.ihe.xcpd.XcpdTestUtils

/**
 * Tests for ITI-55.
 * @author Dmytro Rud
 */
class TestIti55 extends StandardTestContainer {

    final String SERVICE1_URI = "xcpd-iti55://localhost:${port}/iti55service?correlator=#correlator"
    final String SERVICE1_RESPONSE_URI = "http://localhost:${port}/iti55service-response"
    final String SERVICE2_URI = "xcpd-iti55://localhost:${port}/iti55service2"

    static final String REQUEST = XcpdTestUtils.readFile('iti55/iti55-sample-request.xml')

    static final Set<Integer> CALLS_WITH_TTL_HEADER = [1, 5, 9] as Set
    static final AtomicInteger ttlResponsesCount = new AtomicInteger(0)

    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), 'iti55/iti-55.xml')
    }

    /**
     * Test whether:
     * <ol>
     *   <li> sync and async requests are possible...
     *   <li> ...and not influence each other (they shouldn't),
     *   <li> async requests are really async (exchanges are InOnly and delays do not matter),
     *   <li> SOAP headers (WSA ReplyTo + TTL) can be set and read,
     *   <li> XSD and Schematron validations work...
     *   <li> ...and the messages are valid either,
     *   <li> ATNA auditing works.
     * </ol>
     */
    @Test
    void testIti55() {
        final int N = 5
        int i = 0

        N.times {
            send(SERVICE1_URI, i++, SERVICE1_RESPONSE_URI)
            send(SERVICE1_URI, i++)
        }

        // wait for completion of asynchronous routes
        Thread.currentThread().sleep(1000 + Iti55TestRouteBuilder.ASYNC_DELAY)

        assert Iti55TestRouteBuilder.responseCount.get() == N * 2
        assert Iti55TestRouteBuilder.asyncResponseCount.get() == N

        assert auditSender.messages.size() == N * 4
        assert ttlResponsesCount.get() == CALLS_WITH_TTL_HEADER.size()
    }


    private void send(
    String endpointUri,
    int n,
    String responseEndpointUri = null) {
        def requestExchange = new DefaultExchange(camelContext)
        requestExchange.in.body = REQUEST

        // set WSA ReplyTo header, when necessary
        if (responseEndpointUri) {
            requestExchange.in.headers[DefaultItiEndpoint.WSA_REPLYTO_HEADER_NAME] = responseEndpointUri
        }

        // set correlation key
        requestExchange.in.headers[DefaultItiEndpoint.CORRELATION_KEY_HEADER_NAME] = "corr ${n}"

        // set TTL SOAP header
        // we do it not on each message to check whether message context is being properly cleared
        // between invocations
        if (n in CALLS_WITH_TTL_HEADER) {
            XcpdTestUtils.setTtl(requestExchange.in, n)
        }

        // set request HTTP headers
        requestExchange.in.headers[DefaultItiEndpoint.OUTGOING_HTTP_HEADERS] =
            ['MyRequestHeader': "Number ${n}".toString()]

        // send and check timing
        long startTimestamp = System.currentTimeMillis()
        def resultMessage = Exchanges.resultMessage(producerTemplate.send(endpointUri, requestExchange))
        // TODO: reactivate test
        //assert (System.currentTimeMillis() - startTimestamp < Iti55TestRouteBuilder.ASYNC_DELAY)

        // for sync messages -- check acknowledgement code and incoming TTL header
        if (!responseEndpointUri) {
            XcpdTestUtils.testPositiveAckCode(resultMessage.body)

            def dura = TtlHeaderUtils.getTtl(resultMessage)
            if (dura) {
                assert dura.toString() == "P${n * 2}Y"
                ttlResponsesCount.incrementAndGet()
            }

            def inHttpHeaders = resultMessage.headers[DefaultItiEndpoint.INCOMING_HTTP_HEADERS]
            assert inHttpHeaders['MyResponseHeader'].startsWith('Re: Number')
        }
    }


    @Test
    void testNakGeneration() {
        def requestExchange = new DefaultExchange(camelContext)
        requestExchange.in.body = '<test />'
        def responseMessage = Exchanges.resultMessage(producerTemplate.send(SERVICE2_URI, requestExchange))
        def response = Hl7v3Utils.slurp(responseMessage.body)

        assert response.acknowledgement.typeCode.@code == 'AE'
        assert response.acknowledgement.acknowledgementDetail.code.@code == 'INTERR'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.code.@code == 'ActAdministrativeDetectedIssueCode'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.mitigatedBy.detectedIssueManagement.code.@code == 'InternalError'
        assert response.controlActProcess.reasonOf.detectedIssueEvent.mitigatedBy.detectedIssueManagement.code.@codeSystem == '1.3.6.1.4.1.19376.1.2.27.3'
        assert response.controlActProcess.queryAck.statusCode.@code == 'aborted'
        assert response.controlActProcess.queryAck.queryResponseCode.@code == 'AE'
    }
}
