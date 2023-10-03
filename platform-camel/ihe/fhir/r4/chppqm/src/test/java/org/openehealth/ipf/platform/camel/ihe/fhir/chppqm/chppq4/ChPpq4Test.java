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

package org.openehealth.ipf.platform.camel.ihe.fhir.chppqm.chppq4;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
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
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmConsentCreator.*;

@Slf4j
public class ChPpq4Test extends FhirTestContainer {

    @BeforeAll
    public static void setUpClass() {
        startServer("ch-ppq4.xml");
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
        List<Consent> consents = List.of(
                create201Consent(createUuid(), "123456789012345678"),
                create202Consent(createUuid(), "123456789012345678", "urn:e-health-suisse:2015:policies:access-level:normal"),
                create203Consent(createUuid(), "123456789012345678", "urn:e-health-suisse:2015:policies:provide-level:restricted"));
        Bundle requestBundle = new Bundle();
        requestBundle.getMeta().addProfile(ChPpqmUtils.Profiles.REQUEST_BUNDLE);
        requestBundle.setId(createUuid());
        requestBundle.setType(Bundle.BundleType.TRANSACTION);
        for (Consent consent : consents) {
            Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
            entry.getRequest().setMethod(Bundle.HTTPVerb.POST);
            entry.getRequest().setUrl("Consent");
            entry.setResource(consent);
            requestBundle.addEntry(entry);
        }

        Exchange exchange = new DefaultExchange(camelContext, ExchangePattern.InOut);
        exchange.getMessage().setBody(requestBundle);
        exchange.getMessage().setHeader(Constants.HTTP_METHOD, "POST");
        exchange.getMessage().setHeader(Constants.HTTP_OUTGOING_HEADERS, Map.of("Connection", List.of("close")));
        exchange = producerTemplate.send("ch-ppq4://localhost:" + DEMO_APP_PORT, exchange);
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
