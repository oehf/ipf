/*
 * Copyright 2026 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.consumer;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.RequestIdHeaders;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.Constants;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Christian Ohr
 */
public class AuditInterceptorUtilsTest {

    private static final String TRACE_ID = "80f198ee56343ba864fe8b2a57d3eff7";

    private final DefaultCamelContext camelContext = new DefaultCamelContext();

    @Test
    public void testExtractsXRequestIdByDefault() {
        var exchange = exchangeWith(Constants.HTTP_INCOMING_HEADERS, "X-Request-Id", "abc");
        assertEquals("abc", AuditInterceptorUtils.extractRequestId(exchange, new DefaultAuditContext()).orElseThrow().value());
    }

    @Test
    public void testHeaderLookupIsCaseInsensitive() {
        var exchange = exchangeWith(Constants.HTTP_INCOMING_HEADERS, "x-request-id", "abc");
        assertEquals("abc", AuditInterceptorUtils.extractRequestId(exchange, new DefaultAuditContext()).orElseThrow().value());
    }

    @Test
    public void testExtractsFromOutgoingHeadersOnTheClientSide() {
        var exchange = exchangeWith(Constants.HTTP_OUTGOING_HEADERS, "X-Request-Id", "abc");
        assertEquals("abc", AuditInterceptorUtils.extractRequestId(exchange, new DefaultAuditContext()).orElseThrow().value());
    }

    @Test
    public void testExtractsConfiguredTracingHeaderInstead() {
        var auditContext = new DefaultAuditContext();
        auditContext.setRequestIdHeaderNames(List.of("traceparent"));

        var exchange = exchangeWith(Constants.HTTP_INCOMING_HEADERS,
            Map.of("traceparent", List.of("00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01"),
                "X-Request-Id", List.of("abc")));

        assertEquals("00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01",
            AuditInterceptorUtils.extractRequestId(exchange, auditContext).orElseThrow().value());
    }

    @Test
    public void testConfiguredHeadersAreTriedInOrder() {
        var auditContext = new DefaultAuditContext();
        auditContext.setRequestIdHeaderNames(List.of("traceparent", "X-Request-Id"));

        var exchange = exchangeWith(Constants.HTTP_INCOMING_HEADERS, "X-Request-Id", "abc");
        assertEquals("abc", AuditInterceptorUtils.extractRequestId(exchange, auditContext).orElseThrow().value());
    }

    @Test
    public void testExtractsTheTraceIdFromAB3SingleHeader() {
        var auditContext = new DefaultAuditContext();
        auditContext.setRequestIdHeaderNames(List.of(RequestIdHeaders.B3, RequestIdHeaders.X_B3_TRACE_ID));

        var exchange = exchangeWith(Constants.HTTP_INCOMING_HEADERS,
            RequestIdHeaders.B3, TRACE_ID + "-e457b5a2e4d86bd1-1");

        assertEquals(TRACE_ID, AuditInterceptorUtils.extractRequestId(exchange, auditContext).orElseThrow().value());
    }

    @Test
    public void testFallsThroughToB3MultiHeaderWhenTheSingleHeaderCarriesNoId() {
        var auditContext = new DefaultAuditContext();
        auditContext.setRequestIdHeaderNames(List.of(RequestIdHeaders.B3, RequestIdHeaders.X_B3_TRACE_ID));

        // "b3: 0" denies sampling and propagates no ids, so the multi-header flavour is used instead
        var exchange = exchangeWith(Constants.HTTP_INCOMING_HEADERS,
            Map.of(RequestIdHeaders.B3, List.of("0"),
                RequestIdHeaders.X_B3_TRACE_ID, List.of(TRACE_ID)));

        assertEquals(TRACE_ID, AuditInterceptorUtils.extractRequestId(exchange, auditContext).orElseThrow().value());
    }

    @Test
    public void testTheParticipantObjectIdTypeFollowsTheHeader() {
        var auditContext = new DefaultAuditContext();
        auditContext.setRequestIdHeaderNames(List.of(
            RequestIdHeaders.TRACEPARENT, RequestIdHeaders.B3, RequestIdHeaders.X_B3_TRACE_ID,
            RequestIdHeaders.X_REQUEST_ID));

        assertEquals(ParticipantObjectIdTypeCode.W3cTraceContext,
            typeFoundIn(auditContext, RequestIdHeaders.TRACEPARENT, "00-" + TRACE_ID + "-b7ad6b7169203331-01"));
        assertEquals(ParticipantObjectIdTypeCode.B3SingleHeader,
            typeFoundIn(auditContext, RequestIdHeaders.B3, TRACE_ID + "-b7ad6b7169203331-1"));
        assertEquals(ParticipantObjectIdTypeCode.B3MultiHeader,
            typeFoundIn(auditContext, RequestIdHeaders.X_B3_TRACE_ID, TRACE_ID));
        assertEquals(ParticipantObjectIdTypeCode.XRequestId,
            typeFoundIn(auditContext, RequestIdHeaders.X_REQUEST_ID, "abc"));
    }

    @Test
    public void testUnknownHeadersFallBackToTheBalpType() {
        var auditContext = new DefaultAuditContext();
        auditContext.setRequestIdHeaderNames(List.of("X-Gateway-Correlation-Id"));

        assertEquals(ParticipantObjectIdTypeCode.XRequestId,
            typeFoundIn(auditContext, "X-Gateway-Correlation-Id", "gw-42"));
    }

    @Test
    public void testAConfiguredTypeFlattensTheDistinction() {
        var auditContext = new DefaultAuditContext();
        auditContext.setRequestIdHeaderNames(List.of(RequestIdHeaders.B3, RequestIdHeaders.TRACEPARENT));
        auditContext.setRequestIdType(ParticipantObjectIdTypeCode.XRequestId);

        assertEquals(ParticipantObjectIdTypeCode.XRequestId,
            typeFoundIn(auditContext, RequestIdHeaders.B3, TRACE_ID + "-b7ad6b7169203331-1"));
        assertEquals(ParticipantObjectIdTypeCode.XRequestId,
            typeFoundIn(auditContext, RequestIdHeaders.TRACEPARENT, "00-" + TRACE_ID + "-b7ad6b7169203331-01"));
    }

    private ParticipantObjectIdType typeFoundIn(DefaultAuditContext auditContext, String headerName, String headerValue) {
        var auditDataset = auditDataset();
        AuditInterceptorUtils.enrichAuditDatasetFromRequest(auditDataset, auditContext,
            exchangeWith(Constants.HTTP_INCOMING_HEADERS, headerName, headerValue));
        return auditDataset.getRequestIdType();
    }

    @Test
    public void testEmptyWithoutAnyOfTheConfiguredHeaders() {
        var exchange = exchangeWith(Constants.HTTP_INCOMING_HEADERS, "Content-Type", "application/fhir+xml");
        assertTrue(AuditInterceptorUtils.extractRequestId(exchange, new DefaultAuditContext()).isEmpty());
    }

    @Test
    public void testEmptyWhenTheLookupIsDisabled() {
        var auditContext = new DefaultAuditContext();
        auditContext.setRequestIdHeaderNames(List.of());

        var exchange = exchangeWith(Constants.HTTP_INCOMING_HEADERS, "X-Request-Id", "abc");
        assertTrue(AuditInterceptorUtils.extractRequestId(exchange, auditContext).isEmpty());
    }

    @Test
    public void testEmptyWithoutAnyHttpHeaders() {
        assertTrue(AuditInterceptorUtils.extractRequestId(new DefaultExchange(camelContext), new DefaultAuditContext())
            .isEmpty());
    }

    private AuditDataset auditDataset() {
        return new AuditDataset(true) {
            @Override public String getSourceUserId() { return null; }
            @Override public String getDestinationUserId() { return null; }
            @Override public String getLocalAddress() { return null; }
            @Override public String getRemoteAddress() { return null; }
            @Override public List<HumanUser> getHumanUsers() { return List.of(); }
        };
    }

    private Exchange exchangeWith(String headerMapName, String headerName, String headerValue) {
        return exchangeWith(headerMapName, Map.of(headerName, List.of(headerValue)));
    }

    private Exchange exchangeWith(String headerMapName, Map<String, List<String>> httpHeaders) {
        var exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(headerMapName, httpHeaders);
        return exchange;
    }

}
