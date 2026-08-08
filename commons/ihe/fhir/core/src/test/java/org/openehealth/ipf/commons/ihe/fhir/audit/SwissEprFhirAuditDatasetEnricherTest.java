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
package org.openehealth.ipf.commons.ihe.fhir.audit;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.HTTP_INCOMING_HEADERS;

/**
 * @author Christian Ohr
 */
public class SwissEprFhirAuditDatasetEnricherTest {

    private static final String TRACEPARENT = "00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01";
    private static final String X_REQUEST_ID = "d1f7a0f0-0f5f-4a6b-9a3f-8c2f1f2d4e5a";

    private final SwissEprFhirAuditDatasetEnricher enricher = new SwissEprFhirAuditDatasetEnricher();

    @Test
    public void testExtractsTheTraceContextIdUnderTheSwissCode() {
        var auditDataset = auditDataset();
        enricher.enrichAuditDatasetFromRequest(auditDataset, null, incomingTraceparent());

        assertEquals(TRACEPARENT, auditDataset.getRequestId());
        assertEquals(ParticipantObjectIdTypeCode.SwissW3cTraceContext, auditDataset.getRequestIdType());
    }

    @Test
    public void testOverridesWhatTheGenericExtractionFound() {
        // the interceptor runs the generic header lookup before the enricher; the enricher is the more
        // specific, explicitly configured mechanism and has to win
        var auditDataset = auditDataset();
        auditDataset.setRequestId(X_REQUEST_ID, ParticipantObjectIdTypeCode.XRequestId);

        enricher.enrichAuditDatasetFromRequest(auditDataset, null, incomingTraceparent());

        assertEquals(TRACEPARENT, auditDataset.getRequestId());
        assertEquals(ParticipantObjectIdTypeCode.SwissW3cTraceContext, auditDataset.getRequestIdType());
    }

    @Test
    public void testTheResponsePassDoesNotOverwriteTheRequestPass() {
        var auditDataset = auditDataset();
        enricher.enrichAuditDatasetFromRequest(auditDataset, null, incomingTraceparent());
        enricher.enrichAuditDatasetFromResponse(auditDataset, null,
            Map.of(HTTP_INCOMING_HEADERS, Map.of("traceparent", List.of("00-other-other-01"))));

        assertEquals(TRACEPARENT, auditDataset.getRequestId());
    }

    @Test
    public void testLeavesAGenericCorrelationIdAloneWithoutATraceparent() {
        var auditDataset = auditDataset();
        auditDataset.setRequestId(X_REQUEST_ID, ParticipantObjectIdTypeCode.XRequestId);

        enricher.enrichAuditDatasetFromRequest(auditDataset, null,
            Map.of(HTTP_INCOMING_HEADERS, Map.of("Content-Type", List.of("application/fhir+xml"))));

        assertEquals(X_REQUEST_ID, auditDataset.getRequestId());
        assertEquals(ParticipantObjectIdTypeCode.XRequestId, auditDataset.getRequestIdType());
    }

    private Map<String, Object> incomingTraceparent() {
        return Map.of(HTTP_INCOMING_HEADERS, Map.of("traceparent", List.of(TRACEPARENT)));
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

}
