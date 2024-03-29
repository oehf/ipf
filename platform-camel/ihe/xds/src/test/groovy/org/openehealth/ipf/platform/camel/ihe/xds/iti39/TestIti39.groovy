/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.iti39

import org.apache.camel.support.DefaultExchange
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer
import org.springframework.test.annotation.DirtiesContext

import java.util.concurrent.TimeUnit

/**
 * Tests for ITI-39.
 * @author Dmytro Rud
 */
@DirtiesContext
class TestIti39 extends XdsStandardTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti-39.xml'
    
    final String SERVICE1_URI =
            "xca-iti39://localhost:${port}/iti39service" +
            '?correlator=#correlator' +
            '&inInterceptors=#clientSyncInLogger' +
            '&inFaultInterceptors=#clientSyncInLogger' +
            '&outInterceptors=#clientSyncOutLogger' +
            '&outFaultInterceptors=#clientSyncOutLogger'

    final String SERVICE1_RESPONSE_URI = "http://localhost:${port}/iti39service-response"

    final String SERVICE2_SPLIT_AUDIT_URI = "xca-iti39://localhost:${port}/iti39service2-splitAudit"

    static final RetrieveDocumentSet REQUEST = SampleData.createRetrieveDocumentSet()

    static final long AWAIT_DELAY = 20 * 1000L

    static void main(args) {
        System.setProperty(PayloadLoggerBase.PROPERTY_DISABLED, 'true')
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }
    
    @BeforeAll
    static void setUpClass() {
        System.setProperty(PayloadLoggerBase.PROPERTY_DISABLED, 'true')
        // System.setProperty(PayloadLoggerBase.PROPERTY_CONSOLE, 'true')
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }
    
    /**
     * Test whether:
     * <ol>
     *   <li> sync and async requests are possible...
     *   <li> ...and not influence each other (they shouldn't),
     *   <li> async requests are really async (exchanges are InOnly and delays do not matter),
     *   <li> SOAP headers (WSA ReplyTo) can be set and read,
     *   <li> XSD and Schematron validations work...
     *   <li> ...and the messages are valid either,
     *   <li> ATNA auditing works.
     * </ol>
     */
    @Test
    void testIti39() {
        final int N = Iti39TestRouteBuilder.TASKS_COUNT
        int i = 0
        
        N.times {
            send(SERVICE1_URI, i++, SERVICE1_RESPONSE_URI)
            send(SERVICE1_URI, i++)
        }

        // wait for completion of asynchronous routes
        Iti39TestRouteBuilder routeBuilder = appContext.getBean(Iti39TestRouteBuilder.class)
        routeBuilder.countDownLatch.await(AWAIT_DELAY, TimeUnit.MILLISECONDS)
        routeBuilder.asyncCountDownLatch.await(AWAIT_DELAY, TimeUnit.MILLISECONDS)

        assert Iti39TestRouteBuilder.responseCount.get() == N * 2
        assert Iti39TestRouteBuilder.asyncResponseCount.get() == N
        
        assert auditSender.messages.size() == N * 4
        
        assert ! Iti39TestRouteBuilder.errorOccurred
    }

    /**
     * Requested are two documents, received are two as well, but one with a wrong ID.
     * Per actor, two audit records shall be generated -- one with status SUCCESS and IDs
     * of delivered documents (one as requested, one unsolicited); another one with
     * status FAILURE and the ID of the requested, but not delivered document.
     */
    @Test
    void testSplittingAuditRecords() {
        def requestExchange = new DefaultExchange(camelContext)
        requestExchange.in.body = REQUEST
        producerTemplate.send(SERVICE2_SPLIT_AUDIT_URI, requestExchange)
        assert auditSender.messages.size() == 4
    }
    
    
    private void send(
            String endpointUri,
            int n,
            String responseEndpointUri = null)
    {
        def requestExchange = new DefaultExchange(camelContext)
        requestExchange.in.body = REQUEST
        
        // set WSA ReplyTo header, when necessary
        if (responseEndpointUri) {
            requestExchange.in.headers[AbstractWsEndpoint.WSA_REPLYTO_HEADER_NAME] = responseEndpointUri
        }
        
        // set correlation key
        requestExchange.in.headers[AbstractWsEndpoint.CORRELATION_KEY_HEADER_NAME] = "corr ${n}"
        
        // set request HTTP headers
        requestExchange.in.headers[AbstractWsEndpoint.OUTGOING_HTTP_HEADERS] =
                ['MyRequestHeader': "Number ${n}".toString()]
        
        // send and check timing
        long startTimestamp = System.currentTimeMillis()
        def resultMessage = producerTemplate.send(endpointUri, requestExchange).message
        // TODO: reactivate test
        //assert (System.currentTimeMillis() - startTimestamp < Iti39TestRouteBuilder.ASYNC_DELAY)
        
        // for sync messages -- check acknowledgement code and incoming TTL header
        if (!responseEndpointUri) {
            assert resultMessage.getBody(RetrievedDocumentSet.class).status == Status.SUCCESS
            
            def inHttpHeaders = resultMessage.headers[AbstractWsEndpoint.INCOMING_HTTP_HEADERS]
            assert inHttpHeaders['MyResponseHeader'].startsWith('Re: Number')
        }
    }
    
}
