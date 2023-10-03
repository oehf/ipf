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

package org.openehealth.ipf.platform.camel.ihe.fhir.chppqm.chppq5;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.support.DefaultExchange;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Consent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareMethanolRestfulClientFactory;
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ChPpq5Test extends FhirTestContainer {

    @BeforeAll
    public static void setUpClass() {
        startServer("ch-ppq5.xml");
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

    @Test
    public void test1() throws Exception {
        Bundle response = client.search()
                .forResource(Consent.class)
                .where(Consent.IDENTIFIER.exactly().identifier(UUID.randomUUID().toString()))
                .where(Map.of("patient:identifier", List.of(new TokenParam(PpqConstants.CodingSystemIds.SWISS_PATIENT_ID, "123456789012345678"))))
                .returnBundle(Bundle.class)
                .encodedJson()
                .execute();

        List<AuditMessage> auditMessages = auditSender.getMessages();
        assertEquals(1, auditMessages.size());
        log.info("");
    }

    @Test
    public void test2() throws Exception {
        ICriterion<?>[] criteria = {
                Consent.IDENTIFIER.exactly().identifier(UUID.randomUUID().toString()),
                new TokenClientParam("patient:identifier").exactly().systemAndIdentifier(PpqConstants.CodingSystemIds.SWISS_PATIENT_ID, "123456789012345678")
        };

        Exchange exchange = new DefaultExchange(camelContext, ExchangePattern.InOut);
        exchange.getMessage().setBody(criteria);
        exchange.getMessage().setHeader(Constants.HTTP_METHOD, "POST");
        exchange.getMessage().setHeader(Constants.HTTP_OUTGOING_HEADERS, Map.of("Connection", List.of("close")));
        exchange = producerTemplate.send("ch-ppq5://localhost:" + DEMO_APP_PORT, exchange);
        Exception exception = Exchanges.extractException(exchange);
        if (exception != null) {
            throw exception;
        }

        Bundle responseBundle = exchange.getMessage().getMandatoryBody(Bundle.class);

        List<AuditMessage> auditMessages = auditSender.getMessages();
        assertEquals(2, auditMessages.size());
        log.info("");
    }

}
