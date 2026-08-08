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

import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;

import java.util.Optional;

/**
 * The HTTP headers a request may carry the id correlating the audit records of its two ends in, and how
 * to read that id out of them. Which of them are looked at is configured by
 * {@link AuditContext#getRequestIdHeaderNames()}.
 * <p>
 * Most such headers carry nothing but the id, so the header value <em>is</em> the id. The exception is
 * the <a href="https://github.com/openzipkin/b3-propagation#single-header">B3 single header</a>, which
 * packs the trace id, the span id and the sampling state into one value -- and the span id differs
 * between the two ends of a transaction, so only its trace id correlates them. Taking that trace id also
 * makes the single-header and the multi-header flavour of B3 interchangeable: both then yield the same
 * id, which is what {@link #X_B3_TRACE_ID} carries on its own.
 * <p>
 * {@link #TRACEPARENT} is deliberately <em>not</em> reduced to its trace id: it is recorded whole, which
 * is how IPF has always recorded it. Both ends of a transaction see the same header value on the wire,
 * so it correlates them either way.
 * <p>
 * Which header an id came from is kept in the audit record, as the participant object ID type the id is
 * recorded under -- see {@link #participantObjectIdType(String)}. A deployment that would rather record
 * one type whatever the propagation format can fix it with {@link AuditContext#getRequestIdType()}.
 *
 * @author Christian Ohr
 * @since 5.3
 */
public abstract class RequestIdHeaders {

    /** The header IHE BALP asks for. Carries the correlation id and nothing else. */
    public static final String X_REQUEST_ID = "X-Request-Id";

    /** <a href="https://www.w3.org/TR/trace-context/">W3C Trace Context</a>. Recorded whole. */
    public static final String TRACEPARENT = "traceparent";

    /** B3 single header, {@code {TraceId}-{SpanId}[-{SamplingState}[-{ParentSpanId}]]} or {@code 0}. */
    public static final String B3 = "b3";

    /** Trace id of the B3 multi-header flavour. Carries the correlation id and nothing else. */
    public static final String X_B3_TRACE_ID = "X-B3-TraceId";

    /** Value of the B3 single header when the caller denies sampling: no ids are propagated. */
    private static final String B3_DENY_SAMPLING = "0";

    private static final char B3_SEPARATOR = '-';

    /**
     * The participant object ID type a correlation id from the given header is conventionally recorded
     * under. It names the propagation format the id was taken from, which is what tells a reader how to
     * line the id up against the traces it has.
     *
     * @param headerName name of the header the id came from, matched case-insensitively
     * @return the type for it, or {@link ParticipantObjectIdTypeCode#XRequestId} for a header without a
     *      code of its own
     */
    public static ParticipantObjectIdType participantObjectIdType(String headerName) {
        if (TRACEPARENT.equalsIgnoreCase(headerName)) {
            return ParticipantObjectIdTypeCode.W3cTraceContext;
        }
        if (B3.equalsIgnoreCase(headerName)) {
            return ParticipantObjectIdTypeCode.B3SingleHeader;
        }
        if (X_B3_TRACE_ID.equalsIgnoreCase(headerName)) {
            return ParticipantObjectIdTypeCode.B3MultiHeader;
        }
        return ParticipantObjectIdTypeCode.XRequestId;
    }

    /**
     * Reads the correlation id out of a header value.
     *
     * @param headerName  name of the header the value came from, matched case-insensitively
     * @param headerValue value of the header, may be null or blank
     * @return the correlation id, or empty if the header carries none
     */
    public static Optional<String> extractRequestId(String headerName, String headerValue) {
        if (headerValue == null || headerValue.isBlank()) {
            return Optional.empty();
        }
        var value = headerValue.trim();
        return B3.equalsIgnoreCase(headerName) ?
            b3TraceId(value) :
            Optional.of(value);
    }

    /**
     * @param value value of a B3 single header
     * @return its trace id, or empty when the caller denied sampling and propagated no ids at all
     */
    private static Optional<String> b3TraceId(String value) {
        if (B3_DENY_SAMPLING.equals(value)) {
            return Optional.empty();
        }
        var separator = value.indexOf(B3_SEPARATOR);
        if (separator < 0) {
            // a lone token is not a well-formed B3 single header; record it rather than lose it
            return Optional.of(value);
        }
        var traceId = value.substring(0, separator);
        return traceId.isEmpty() ? Optional.empty() : Optional.of(traceId);
    }

}
