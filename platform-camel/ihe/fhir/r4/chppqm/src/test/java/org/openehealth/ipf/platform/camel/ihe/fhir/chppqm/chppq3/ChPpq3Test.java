/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.chppqm.chppq3;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.support.DefaultExchange;
import org.hl7.fhir.r4.model.Consent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareMethanolRestfulClientFactory;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmConsentCreator.create201Consent;
import static org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmConsentCreator.createUuid;

@Slf4j
public class ChPpq3Test extends FhirTestContainer {

    @BeforeAll
    public static void setUpClass() {
        startServer("ch-ppq3.xml");
    }

    public static void startServer(String contextDescriptor) {
        var servlet = new IpfFhirServlet(FhirVersionEnum.R4);
        startServer(servlet, contextDescriptor, false, DEMO_APP_PORT, "FhirServlet");

        var loggingInterceptor = new LoggingInterceptor();
        loggingInterceptor.setLogRequestSummary(false);
        loggingInterceptor.setLogRequestHeaders(true);
        loggingInterceptor.setLogResponseBody(true);
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT), fhirContext -> {
            var clientFactory = new SslAwareMethanolRestfulClientFactory(fhirContext);
            clientFactory.setAsync(true);
            fhirContext.setRestfulClientFactory(clientFactory);
        }).registerInterceptor(loggingInterceptor);
    }

    private Exchange sendPpq3Request(Object request, String httpMethod) throws Exception {
        Exchange exchange = new DefaultExchange(camelContext, ExchangePattern.InOut);
        exchange.getMessage().setBody(request);
        exchange.getMessage().setHeader(Constants.HTTP_METHOD, httpMethod);
        exchange.getMessage().setHeader(Constants.HTTP_OUTGOING_HEADERS, Map.of(
                "Authorization", List.of("Bearer d2h5IGFyZSB5b3UgcmVhZGluZyB0aGlzPw=="),
                "Header2", List.of("Value1", "Value2", "Value3"),
                "Connection", List.of("close")
        ));
        exchange = producerTemplate.send("ch-ppq3://localhost:" + DEMO_APP_PORT, exchange);
        Exception exception = Exchanges.extractException(exchange);
        if (exception != null) {
            throw exception;
        }
        return exchange;
    }

    @Test
    public void testCreate1() throws Exception {
        Consent consent = create201Consent(createUuid(), "123456789012345678");

        Exchange exchange = sendPpq3Request(consent, "POST");

        MethodOutcome methodOutcome = exchange.getMessage().getMandatoryBody(MethodOutcome.class);
        assertTrue(methodOutcome.getCreated());
        Map<String, List<String>> httpHeaders = methodOutcome.getResponseHeaders();
        assertEquals(List.of("value1", "value2"), httpHeaders.get("responseheader2"));

        List<AuditMessage> auditMessages = auditSender.getMessages();
        assertEquals(2, auditMessages.size());
        log.info("");
    }

    @Test
    public void testUpdate1() throws Exception {
        Consent consent = create201Consent(createUuid(), "123456789012345678");
        consent.setId(createUuid());

        Exchange exchange = sendPpq3Request(consent, "PUT");

        MethodOutcome methodOutcome = exchange.getMessage().getMandatoryBody(MethodOutcome.class);

        List<AuditMessage> auditMessages = auditSender.getMessages();
        assertEquals(2, auditMessages.size());
        log.info("");
    }

    @Test
    public void testUpdate2() {
        String traceContextId = "00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01";

        Consent consent = create201Consent(createUuid(), "123456789012345678");
        consent.setId(createUuid());

        MethodOutcome methodOutcome = client.update()
                .resource(consent)
                .conditional()
                .where(Consent.IDENTIFIER.exactly().identifier(createUuid()))
                .withAdditionalHeader("Header2", "Value2")
                .withAdditionalHeader("Authorization", "Bearer d2h5IGFyZSB5b3UgcmVhZGluZyB0aGlzPw==")
                .withAdditionalHeader("Header2", "Value3")
                .withAdditionalHeader("TraceParent", traceContextId)
                .withAdditionalHeader("Header2", "Value1")
                .execute();

        List<AuditMessage> auditMessages = auditSender.getMessages();
        assertEquals(1, auditMessages.size());

        AuditMessage auditMessage = auditMessages.get(0);
        assertEquals(3, auditMessage.getParticipantObjectIdentifications().size());
        assertEquals(traceContextId, auditMessage.getParticipantObjectIdentifications().get(0).getParticipantObjectID());

        log.info("");
    }

    @Test
    public void testUpdate3() {
        Consent consent = create201Consent(createUuid(), "123456789012345678");
        consent.setId(createUuid());

        MethodOutcome methodOutcome = client.update()
            .resource(consent)
            .conditional()
            .where(Consent.IDENTIFIER.exactly().identifier(createUuid()))
            .withAdditionalHeader("Header2", "Value2")
            .withAdditionalHeader("Authorization", "Bearer d2h5IGFyZSB5b3UgcmVhZGluZyB0aGlzPw==")
            .withAdditionalHeader("Header2", "Value3")
            .withAdditionalHeader("Header2", "Value1")
            .execute();

        List<AuditMessage> auditMessages = auditSender.getMessages();
        assertEquals(1, auditMessages.size());

        AuditMessage auditMessage = auditMessages.get(0);
        assertEquals(3, auditMessage.getParticipantObjectIdentifications().size());
        assertEquals(ChPpq3TestRouteBuilder.TRACE_CONTEXT_ID, auditMessage.getParticipantObjectIdentifications().get(0).getParticipantObjectID());

        log.info("");
    }

    @Test
    @Disabled
    public void testDelete1() {
        MethodOutcome methodOutcome = client.delete()
                .resourceConditionalByType(Consent.class)
                .where(Consent.IDENTIFIER.exactly().identifier(createUuid()))
                .withAdditionalHeader("Header2", "Value2")
                .withAdditionalHeader("Authorization", "Bearer d2h5IGFyZSB5b3UgcmVhZGluZyB0aGlzPw==")
                .withAdditionalHeader("Header2", "Value3")
                .withAdditionalHeader("Header2", "Value1")
                .execute();

        List<AuditMessage> auditMessages = auditSender.getMessages();
        assertEquals(1, auditMessages.size());
        log.info("");
    }

    @Test
    public void testDelete2() throws Exception {
        String request = createUuid();

        Exchange exchange = sendPpq3Request(request, "DELETE");

        MethodOutcome methodOutcome = exchange.getMessage().getMandatoryBody(MethodOutcome.class);

        List<AuditMessage> auditMessages = auditSender.getMessages();
        assertEquals(2, auditMessages.size());
        log.info("");
    }

}
