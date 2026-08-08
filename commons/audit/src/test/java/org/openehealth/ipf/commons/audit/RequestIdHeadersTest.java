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
package org.openehealth.ipf.commons.audit;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Christian Ohr
 */
public class RequestIdHeadersTest {

    private static final String TRACE_ID = "80f198ee56343ba864fe8b2a57d3eff7";
    private static final String SPAN_ID = "e457b5a2e4d86bd1";
    private static final String PARENT_SPAN_ID = "05e3ac9a4f6e3b90";

    @Test
    public void testXRequestIdIsTakenAsIs() {
        assertEquals("abc-123",
            RequestIdHeaders.extractRequestId(RequestIdHeaders.X_REQUEST_ID, "abc-123").orElseThrow());
    }

    @Test
    public void testTraceparentIsRecordedWhole() {
        // not reduced to its trace id: the Swiss EPR has always recorded the whole header
        var traceparent = "00-" + TRACE_ID + "-" + SPAN_ID + "-01";
        assertEquals(traceparent,
            RequestIdHeaders.extractRequestId(RequestIdHeaders.TRACEPARENT, traceparent).orElseThrow());
    }

    @Test
    public void testB3MultiHeaderTraceIdIsTakenAsIs() {
        assertEquals(TRACE_ID,
            RequestIdHeaders.extractRequestId(RequestIdHeaders.X_B3_TRACE_ID, TRACE_ID).orElseThrow());
    }

    @Test
    public void testB3SingleHeaderYieldsTheTraceId() {
        assertEquals(TRACE_ID, RequestIdHeaders.extractRequestId(
            RequestIdHeaders.B3, TRACE_ID + "-" + SPAN_ID).orElseThrow());
    }

    @Test
    public void testB3SingleHeaderWithSamplingState() {
        assertEquals(TRACE_ID, RequestIdHeaders.extractRequestId(
            RequestIdHeaders.B3, TRACE_ID + "-" + SPAN_ID + "-1").orElseThrow());
    }

    @Test
    public void testB3SingleHeaderWithParentSpanId() {
        assertEquals(TRACE_ID, RequestIdHeaders.extractRequestId(
            RequestIdHeaders.B3, TRACE_ID + "-" + SPAN_ID + "-1-" + PARENT_SPAN_ID).orElseThrow());
    }

    @Test
    public void testB3SingleAndMultiHeaderYieldTheSameId() {
        assertEquals(
            RequestIdHeaders.extractRequestId(RequestIdHeaders.X_B3_TRACE_ID, TRACE_ID),
            RequestIdHeaders.extractRequestId(RequestIdHeaders.B3, TRACE_ID + "-" + SPAN_ID + "-1"));
    }

    @Test
    public void testB3SingleHeaderDenyingSamplingCarriesNoId() {
        assertTrue(RequestIdHeaders.extractRequestId(RequestIdHeaders.B3, "0").isEmpty());
    }

    @Test
    public void testB3HeaderNameIsMatchedCaseInsensitively() {
        assertEquals(TRACE_ID,
            RequestIdHeaders.extractRequestId("B3", TRACE_ID + "-" + SPAN_ID).orElseThrow());
    }

    @Test
    public void testMalformedB3SingleHeaderIsRecordedRatherThanDropped() {
        assertEquals("whatever", RequestIdHeaders.extractRequestId(RequestIdHeaders.B3, "whatever").orElseThrow());
    }

    @Test
    public void testB3SingleHeaderWithoutATraceIdCarriesNoId() {
        assertTrue(RequestIdHeaders.extractRequestId(RequestIdHeaders.B3, "-" + SPAN_ID).isEmpty());
    }

    @Test
    public void testBlankAndMissingValuesCarryNoId() {
        assertTrue(RequestIdHeaders.extractRequestId(RequestIdHeaders.X_REQUEST_ID, null).isEmpty());
        assertTrue(RequestIdHeaders.extractRequestId(RequestIdHeaders.X_REQUEST_ID, "   ").isEmpty());
        assertTrue(RequestIdHeaders.extractRequestId(RequestIdHeaders.B3, "").isEmpty());
    }

    @Test
    public void testValuesAreTrimmed() {
        assertEquals("abc", RequestIdHeaders.extractRequestId(RequestIdHeaders.X_REQUEST_ID, "  abc  ").orElseThrow());
    }

    @Test
    public void testUnknownHeadersAreTakenAtFaceValue() {
        assertEquals("gw-42", RequestIdHeaders.extractRequestId("X-Gateway-Correlation-Id", "gw-42").orElseThrow());
    }

    @Test
    public void testEachPropagationFormatHasItsOwnParticipantObjectIdType() {
        assertEquals(ParticipantObjectIdTypeCode.XRequestId,
            RequestIdHeaders.participantObjectIdType(RequestIdHeaders.X_REQUEST_ID));
        assertEquals(ParticipantObjectIdTypeCode.W3cTraceContext,
            RequestIdHeaders.participantObjectIdType(RequestIdHeaders.TRACEPARENT));
        assertEquals(ParticipantObjectIdTypeCode.B3SingleHeader,
            RequestIdHeaders.participantObjectIdType(RequestIdHeaders.B3));
        assertEquals(ParticipantObjectIdTypeCode.B3MultiHeader,
            RequestIdHeaders.participantObjectIdType(RequestIdHeaders.X_B3_TRACE_ID));
    }

    @Test
    public void testParticipantObjectIdTypeLookupIsCaseInsensitive() {
        assertEquals(ParticipantObjectIdTypeCode.B3MultiHeader,
            RequestIdHeaders.participantObjectIdType("x-b3-traceid"));
    }

    @Test
    public void testAnUnknownHeaderFallsBackToTheBalpType() {
        assertEquals(ParticipantObjectIdTypeCode.XRequestId,
            RequestIdHeaders.participantObjectIdType("X-Gateway-Correlation-Id"));
    }

}
