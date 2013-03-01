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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55;

import java.util.concurrent.atomic.AtomicInteger

import org.apache.camel.Exchange
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.impl.DefaultExchange
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55Utils
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.hl7v3.MyRejectionHandlingStrategy

import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import org.springframework.test.annotation.DirtiesContext

/**
 * Tests for ITI-55.
 * @author Dmytro Rud
 */
@DirtiesContext
class TestIti55 extends StandardTestContainer {
    enum RequestType {REGULAR, ASYNC, DEFERRED}

    def static CONTEXT_DESCRIPTOR = 'iti55/iti-55.xml'
    
    final String SERVICE1_URI = "xcpd-iti55://localhost:${port}/iti55service?correlator=#correlator"
    final String SERVICE2_URI = "xcpd-iti55://localhost:${port}/iti55service2"

    final String SERVICE1_ASYNC_RESPONSE_URI = "http://localhost:${port}/iti55service-async-response"
    final String SERVICE1_DEFERRED_RESPONSE_URI = "http://localhost:${port}/iti55service-deferred-response"

    final String REQUEST = StandardTestContainer.readFile('iti55/iti55-sample-request.xml')
    
    final String REQUEST_DEFERRED =
        StandardTestContainer.readFile('iti55/iti55-sample-request-deferred.xml').replace(
                '***REPLACEME***', SERVICE1_DEFERRED_RESPONSE_URI)


    static final Set<Integer> CALLS_WITH_TTL_HEADER = [1, 7, 13] as Set
    static final AtomicInteger ttlResponsesCount = new AtomicInteger(0)
    
    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }
    
    private MockEndpoint allResponsesMockEndpoint
    private MockEndpoint asyncResponsesMockEndpoint
    private MockEndpoint deferredResponsesMockEndpoint

    static Set<String> CORRELATION_KEYS = Collections.synchronizedSet(new HashSet<String>())

    final int REPETITIONS_COUNT = 5

    final long RESULT_WAIT_TIME = 30000L


    @BeforeClass
    static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    @Before
    public void setUp(){
        allResponsesMockEndpoint      = camelContext.getEndpoint('mock:response')
        asyncResponsesMockEndpoint    = camelContext.getEndpoint('mock:asyncResponse')
        deferredResponsesMockEndpoint = camelContext.getEndpoint('mock:deferredResponse')

        MockEndpoint.resetMocks(camelContext)

        CORRELATION_KEYS.clear()
        int i = 0
        REPETITIONS_COUNT.times {
            CORRELATION_KEYS << "ASYNC-${i++}".toString()
            i++
            CORRELATION_KEYS << "DEFERRED-${i++}".toString()
        }
    }
    
    
    /**
     * Test whether:
     * <ol>
     *   <li> sync, async and deferred mode requests are possible...
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
        // how many responses of different types do we expect?
        allResponsesMockEndpoint.expectedMessageCount      = REPETITIONS_COUNT * 3
        asyncResponsesMockEndpoint.expectedMessageCount    = REPETITIONS_COUNT
        deferredResponsesMockEndpoint.expectedMessageCount = REPETITIONS_COUNT


        allResponsesMockEndpoint.resultWaitTime      = RESULT_WAIT_TIME
        asyncResponsesMockEndpoint.resultWaitTime    = RESULT_WAIT_TIME
        deferredResponsesMockEndpoint.resultWaitTime = RESULT_WAIT_TIME

        int i = 0
        REPETITIONS_COUNT.times {
            sendMainTestMessage(RequestType.ASYNC,    i++)
            sendMainTestMessage(RequestType.REGULAR,  i++)
            sendMainTestMessage(RequestType.DEFERRED, i++)
        }

        // wait for completion of asynchronous routes
        allResponsesMockEndpoint.assertIsSatisfied()
        asyncResponsesMockEndpoint.assertIsSatisfied()
        deferredResponsesMockEndpoint.assertIsSatisfied()

        assert CORRELATION_KEYS.empty

        assert auditSender.messages.size() == REPETITIONS_COUNT * 6
        assert ttlResponsesCount.get() == CALLS_WITH_TTL_HEADER.size()
        
        assert ! Iti55TestRouteBuilder.errorOccurred
    }
    
    
    private void sendMainTestMessage(RequestType requestType, int n) {
        Exchange requestExchange = new DefaultExchange(camelContext)
        requestExchange.in.body = (requestType == RequestType.DEFERRED) ? REQUEST_DEFERRED : REQUEST
        
        // set WSA ReplyTo header, when necessary
        if (requestType == RequestType.ASYNC) {
            requestExchange.in.headers[AbstractWsEndpoint.WSA_REPLYTO_HEADER_NAME] = SERVICE1_ASYNC_RESPONSE_URI
        }
        
        // set correlation key
        requestExchange.in.headers[AbstractWsEndpoint.CORRELATION_KEY_HEADER_NAME] = "${requestType}-${n}"
        
        // set TTL SOAP header
        // we do it not on each message to check whether message context is being properly cleared
        // between invocations
        if (n in CALLS_WITH_TTL_HEADER) {
            XcpdTestUtils.setTtl(requestExchange.in, n)
        }
        
        // set request HTTP headers
        requestExchange.in.headers[AbstractWsEndpoint.OUTGOING_HTTP_HEADERS] =
                ['MyRequestHeader': "Number ${n}".toString()]
        
        // send and check timing
        long startTimestamp = System.currentTimeMillis()
        def resultMessage = Exchanges.resultMessage(producerTemplate.send(SERVICE1_URI, requestExchange))
        // TODO: reactivate test
        //assert (System.currentTimeMillis() - startTimestamp < Iti55TestRouteBuilder.ASYNC_DELAY)
        
        // for regular requests -- check acknowledgement code and incoming TTL header
        if (requestType == RequestType.REGULAR) {
            XcpdTestUtils.testPositiveAckCode(resultMessage.body)
            
            def dura = TtlHeaderUtils.getTtl(resultMessage)
            if (dura) {
                assert dura.toString() == "P${n * 2}Y"
                ttlResponsesCount.incrementAndGet()
            }

            def inHttpHeaders = resultMessage.headers[AbstractWsEndpoint.INCOMING_HTTP_HEADERS]
            assert inHttpHeaders['MyResponseHeader'] == "Re: Number ${n}"
        }

        // for deferred response requests -- check whether a positive MCCI ACK is returned
        if (requestType == RequestType.DEFERRED) {
            assert Iti55Utils.isMcciAck(resultMessage.body)
        }
    }
    
    
    @Test
    void testNakGeneration() {
        def requestExchange = new DefaultExchange(camelContext)
        requestExchange.in.body = REQUEST
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


    /**
     * Send some garbage to an XCPD endpoint (via raw HTTP, because we want to test
     * consumer behaviour and therefore should avoid checks on the producer side),
     * and check whether the rejection handling strategy works well.
     */
    @Test
    void testRejectionHandling() {
        def requestExchange = new DefaultExchange(camelContext)
        requestExchange.in.body = '< some ill-formed XML !'
        Exchanges.resultMessage(producerTemplate.send(
                "http://localhost:${port}/iti55service",
                requestExchange))
        assert MyRejectionHandlingStrategy.count == 1
    }
}