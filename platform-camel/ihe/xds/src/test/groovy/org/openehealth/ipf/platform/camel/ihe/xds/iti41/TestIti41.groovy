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
package org.openehealth.ipf.platform.camel.ihe.xds.iti41

import org.apache.camel.support.DefaultExchange
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.xml.XmlUtils
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import org.openehealth.ipf.platform.camel.ihe.xds.MyRejectionHandlingStrategy
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer
import org.openehealth.ipf.platform.camel.ihe.xds.iti39.Iti39TestRouteBuilder

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import javax.xml.ws.soap.SOAPFaultException
import java.util.concurrent.TimeUnit

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the ITI-41 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
class TestIti41 extends XdsStandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'iti-41.xml'

    static final long AWAIT_DELAY = 20 * 1000L

    final String SERVICE1 = "xds-iti41://localhost:${port}/xds-iti41-service1"
    final String SERVICE2 = "xds-iti41://localhost:${port}/xds-iti41-service2"
    final String SERVICE3 = "xds-iti41://localhost:${port}/xds-iti41-service3"
    final String SERVICE2_ADDR = "http://localhost:${port}/xds-iti41-service2"

    final String SERVICE_ASYNC_URI =
            "xds-iti41://localhost:${port}/iti41service-async" +
                    '?correlator=#correlator' +
                    '&inInterceptors=#clientSyncInLogger' +
                    '&inFaultInterceptors=#clientSyncInLogger' +
                    '&outInterceptors=#clientSyncOutLogger' +
                    '&outFaultInterceptors=#clientSyncOutLogger'

    final String SERVICE_ASYNC_RESPONSE_URI = "http://localhost:${port}/iti41service-async-response"

    final String SERVICE_SOAP_FAULT_UNHANDLED = "xds-iti41://localhost:${port}/soap-fault-unhandled"
    final String SERVICE_SOAP_FAULT_HANDLED   = "xds-iti41://localhost:${port}/soap-fault-handled"

    ProvideAndRegisterDocumentSet request
    DocumentEntry docEntry

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }

    @BeforeAll
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @BeforeEach
    void setUp() {
        request = SampleData.createProvideAndRegisterDocumentSet()
        docEntry = request.documents[0].documentEntry

        appContext.getBean('routeBuilder', Iti41TestRouteBuilder).soapFaultUnhandledEndpoint = SERVICE_SOAP_FAULT_UNHANDLED
    }

    @Test
    void testIti41() {
        assert SUCCESS == sendIt(SERVICE1, 'service 1', 'urn:oid:1.2.1').status
        assert SUCCESS == sendIt(SERVICE2, 'service 2', 'urn:oid:1.2.2').status
        assert auditSender.messages.size() == 4
        checkAudit(EventOutcomeIndicator.Success)
    }

    @Test
    void testIti41FailureAudit() {
        assert FAILURE == sendIt(SERVICE2, 'falsch', 'urn:oid:1.2.2').status
        assert auditSender.messages.size() == 2
        checkAudit(EventOutcomeIndicator.SeriousFailure)
    }

    @Test
    void testIti41ExtraMetadata() {
        // request with extra metadata
        String submissionSetString = readFile('submission-set.xml')
        String pnrRequestString = """
            <urn:ProvideAndRegisterDocumentSetRequest xmlns:urn="urn:ihe:iti:xds-b:2007">
                ${submissionSetString}
                <urn:Document id="Document01">SGVsbG8gTWFyZWshIDotKQ0K</urn:Document>
            </urn:ProvideAndRegisterDocumentSetRequest>
        """
        JAXBContext jaxbContext = JAXBContext.newInstance(ProvideAndRegisterDocumentSetRequestType.class)
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller()
        def requestWithMetadata = unmarshaller.unmarshal(XmlUtils.source(pnrRequestString))
        def response = send(SERVICE3, requestWithMetadata, Response.class)
        assert response.status == SUCCESS

        // request without extra metadata
        response = send(SERVICE3, request, Response.class)
        assert response.status == FAILURE
    }


    /**
     * Send some garbage to an XDS endpoint (via raw HTTP, because we want to test
     * consumer behaviour and therefore should avoid checks on the producer side),
     * and check whether the rejection handling strategy works well.
     */
    @Test
    void testRejectionHandling() {
        def exchange = new DefaultExchange(camelContext)
        exchange.in.body = '< some ill-formed XML !'
        producerTemplate.send("http://localhost:${port}/xds-iti41-service1", exchange)
        assert MyRejectionHandlingStrategy.count == 1
    }

    void checkAudit(EventOutcomeIndicator outcome) {
        AuditMessage message = getAudit(EventActionCode.Create, SERVICE2_ADDR)[0]
        assert message.activeParticipants.size() == 2
        assert message.participantObjectIdentifications.size() == 2
        
        checkEvent(message.eventIdentification, '110107', 'ITI-41', EventActionCode.Create, outcome)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], SERVICE2_ADDR, false)
        checkAuditSource(message.auditSourceIdentification, 'sourceId')
        checkPatient(message.participantObjectIdentifications[0])
        checkSubmissionSet(message.participantObjectIdentifications[1])
        
        message = getAudit(EventActionCode.Read, SERVICE2_ADDR)[0]
        assert message.activeParticipants.size() == 2
        assert message.participantObjectIdentifications.size() == 2
        
        checkEvent(message.eventIdentification, '110106', 'ITI-41', EventActionCode.Read, outcome)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], SERVICE2_ADDR, false)
        checkPatient(message.participantObjectIdentifications[0])
        checkSubmissionSet(message.participantObjectIdentifications[1])
    }

    def sendIt(String endpoint, String value, String targetHomeCommunityId) {
        docEntry.comments = new LocalizedString(value)
        request.targetHomeCommunityId = targetHomeCommunityId
        send(endpoint, request, Response.class)
    }


    @Test
    void testHandlingOfUnhandledSoapFault() {
        Assertions.assertThrows(SOAPFaultException.class, () ->
            sendIt(SERVICE_SOAP_FAULT_UNHANDLED, 'fail', null)
        );
    }

    @Test
    void testHandlingOfHandledSoapFault() {
        assert SUCCESS == sendIt(SERVICE_SOAP_FAULT_HANDLED, 'ok', null).status
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
    void testIti41Async() {
        final int N = Iti41TestRouteBuilder.TASKS_COUNT
        int i = 0

        N.times {
            sendAsync(SERVICE_ASYNC_URI, i++, SERVICE_ASYNC_RESPONSE_URI)
            sendAsync(SERVICE_ASYNC_URI, i++)
        }

        // wait for completion of asynchronous routes
        Iti41TestRouteBuilder routeBuilder = appContext.getBean(Iti41TestRouteBuilder.class)
        routeBuilder.countDownLatch.await(AWAIT_DELAY, TimeUnit.MILLISECONDS)
        routeBuilder.asyncCountDownLatch.await(AWAIT_DELAY, TimeUnit.MILLISECONDS)

        assert Iti41TestRouteBuilder.responseCount.get() == N * 2
        assert Iti41TestRouteBuilder.asyncResponseCount.get() == N

        assert auditSender.messages.size() == N * 4

        assert ! Iti41TestRouteBuilder.errorOccurred
    }



    private void sendAsync(
            String endpointUri,
            int n,
            String responseEndpointUri = null)
    {
        def requestExchange = new DefaultExchange(camelContext)
        requestExchange.in.body = request

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
        def resultMessage = Exchanges.resultMessage(producerTemplate.send(endpointUri, requestExchange))
        // TODO: reactivate test
        //assert (System.currentTimeMillis() - startTimestamp < Iti41TestRouteBuilder.ASYNC_DELAY)

        // for sync messages -- check acknowledgement code and incoming TTL header
        if (!responseEndpointUri) {
            assert resultMessage.getBody(Response.class).status == SUCCESS

            def inHttpHeaders = resultMessage.headers[AbstractWsEndpoint.INCOMING_HTTP_HEADERS]
            assert inHttpHeaders['MyResponseHeader'].startsWith('Re: Number')
        }
    }

}
