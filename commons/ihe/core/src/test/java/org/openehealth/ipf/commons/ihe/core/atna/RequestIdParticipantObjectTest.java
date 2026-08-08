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

package org.openehealth.ipf.commons.ihe.core.atna;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.event.CustomAuditMessageBuilder;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEAuditMessageBuilder;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The id correlating the audit records of the two ends of a transaction goes into a participant object
 * whose code says which propagation format it came from. That the code varies is the only reason the
 * type is carried next to the value.
 *
 * @author Christian Ohr
 */
public class RequestIdParticipantObjectTest {

    private static final String REQUEST_ID = "d1f7a0f0-0f5f-4a6b-9a3f-8c2f1f2d4e5a";
    private static final String TRACEPARENT = "00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01";
    private static final String B3_TRACE_ID = "80f198ee56343ba864fe8b2a57d3eff7";

    @Test
    public void testBalpRequestIdIsRecordedAsXRequestId() {
        var participantObject = requestIdParticipantObject(auditDatasetWith(dataset ->
            dataset.setRequestId(REQUEST_ID, ParticipantObjectIdTypeCode.XRequestId))).orElseThrow();

        assertEquals(REQUEST_ID, participantObject.getParticipantObjectID());
        assertEquals("XrequestId", participantObject.getParticipantObjectIDTypeCode().getCode());
        assertEquals("IHE BALP", participantObject.getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals(ParticipantObjectTypeCode.Other, participantObject.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.ProcessingElement, participantObject.getParticipantObjectTypeCodeRole());
    }

    @Test
    public void testTraceContextIdIsRecordedUnderTheW3cCodeSystem() {
        var participantObject = requestIdParticipantObject(auditDatasetWith(dataset ->
            dataset.setRequestId(TRACEPARENT, ParticipantObjectIdTypeCode.W3cTraceContext))).orElseThrow();

        // trace context is a W3C standard; the Swiss code system name is not the default for it
        assertEquals(TRACEPARENT, participantObject.getParticipantObjectID());
        assertEquals("traceparent", participantObject.getParticipantObjectIDTypeCode().getCode());
        assertEquals("w3c", participantObject.getParticipantObjectIDTypeCode().getCodeSystemName());
    }

    @Test
    public void testSwissTraceContextIdKeepsItsOwnCode() {
        var participantObject = requestIdParticipantObject(auditDatasetWith(dataset ->
            dataset.setRequestId(TRACEPARENT, ParticipantObjectIdTypeCode.SwissW3cTraceContext))).orElseThrow();

        assertEquals(TRACEPARENT, participantObject.getParticipantObjectID());
        assertEquals("traceparent", participantObject.getParticipantObjectIDTypeCode().getCode());
        assertEquals("e-health-suisse", participantObject.getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals("traceparent", participantObject.getParticipantObjectIDTypeCode().getOriginalText());
        assertEquals(ParticipantObjectTypeCode.Other, participantObject.getParticipantObjectTypeCode());
        assertEquals(ParticipantObjectTypeCodeRole.ProcessingElement, participantObject.getParticipantObjectTypeCodeRole());
    }

    @Test
    public void testB3TraceIdKeepsTheCodeOfItsPropagationFormat() {
        var singleHeader = requestIdParticipantObject(auditDatasetWith(dataset ->
            dataset.setRequestId(B3_TRACE_ID, ParticipantObjectIdTypeCode.B3SingleHeader))).orElseThrow();
        assertEquals(B3_TRACE_ID, singleHeader.getParticipantObjectID());
        assertEquals("b3", singleHeader.getParticipantObjectIDTypeCode().getCode());
        assertEquals("openzipkin", singleHeader.getParticipantObjectIDTypeCode().getCodeSystemName());

        var multiHeader = requestIdParticipantObject(auditDatasetWith(dataset ->
            dataset.setRequestId(B3_TRACE_ID, ParticipantObjectIdTypeCode.B3MultiHeader))).orElseThrow();
        // same id either way, only the code says how it was propagated
        assertEquals(B3_TRACE_ID, multiHeader.getParticipantObjectID());
        assertEquals("X-B3-TraceId", multiHeader.getParticipantObjectIDTypeCode().getCode());
        assertEquals("openzipkin", multiHeader.getParticipantObjectIDTypeCode().getCodeSystemName());
    }

    @Test
    public void testValueTravelsWithTheMessageForTheFhirSerialization() {
        assertEquals(TRACEPARENT, auditMessageFor(auditDatasetWith(dataset ->
            dataset.setRequestId(TRACEPARENT, ParticipantObjectIdTypeCode.SwissW3cTraceContext))).getRequestId());
    }

    @Test
    public void testNoParticipantObjectWithoutARequestId() {
        assertTrue(requestIdParticipantObject(auditDatasetWith(dataset -> {})).isEmpty());
    }

    @Test
    public void testDeprecatedTraceContextAccessorsStillWork() {
        var dataset = auditDatasetWith(d -> d.setW3cTraceContextId(TRACEPARENT));
        assertEquals(TRACEPARENT, dataset.getW3cTraceContextId());
        assertEquals(TRACEPARENT, dataset.getRequestId());
        assertEquals(ParticipantObjectIdTypeCode.SwissW3cTraceContext, dataset.getRequestIdType());
    }

    @Test
    public void testDeprecatedTraceContextGetterIgnoresABalpRequestId() {
        assertNull(auditDatasetWith(dataset -> dataset.setRequestId(REQUEST_ID, ParticipantObjectIdTypeCode.XRequestId)).getW3cTraceContextId());
    }

    private Optional<ParticipantObjectIdentificationType> requestIdParticipantObject(AuditDataset auditDataset) {
        return auditMessageFor(auditDataset).getParticipantObjectIdentifications().stream()
            .filter(poi -> ParticipantObjectTypeCodeRole.ProcessingElement == poi.getParticipantObjectTypeCodeRole())
            .findFirst();
    }

    private AuditMessage auditMessageFor(AuditDataset auditDataset) {
        return new TestAuditMessageBuilder(auditDataset).getMessage();
    }

    private AuditDataset auditDatasetWith(Consumer<AuditDataset> customizer) {
        var auditDataset = new AuditDataset(true) {
            @Override public String getSourceUserId() { return "source"; }
            @Override public String getDestinationUserId() { return "destination"; }
            @Override public String getLocalAddress() { return "localhost"; }
            @Override public String getRemoteAddress() { return "remote"; }
            @Override public List<HumanUser> getHumanUsers() { return List.of(); }
        };
        customizer.accept(auditDataset);
        return auditDataset;
    }

    private static class TestAuditMessageBuilder
        extends IHEAuditMessageBuilder<TestAuditMessageBuilder, CustomAuditMessageBuilder> {

        TestAuditMessageBuilder(AuditDataset auditDataset) {
            super(new DefaultAuditContext(), auditDataset, new CustomAuditMessageBuilder(
                EventOutcomeIndicator.Success, null, EventActionCode.Execute, EventIdCode.Query, null));
        }
    }

}
