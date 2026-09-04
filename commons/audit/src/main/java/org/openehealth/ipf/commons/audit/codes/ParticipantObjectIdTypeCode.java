/*
 * Copyright 2017 the original author or authors.
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

package org.openehealth.ipf.commons.audit.codes;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.EnumeratedValueSet;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;

/**
 * Audit Participant Object ID Type Code as specified in
 * <a href="https://dicom.nema.org/medical/dicom/current/output/html/part16.html#sect_CID_404">Part 16, CID 404</a>
 * 1.2.840.10008.6.1.907
 *
 * @author Christian Ohr
 * @since 3.5
 */
public enum ParticipantObjectIdTypeCode implements ParticipantObjectIdType, EnumeratedCodedValue<ParticipantObjectIdType> {

    // RFC-3881
    MedicalRecordNumber("1", "Medical Record Number", "RFC-3881"),
    PatientNumber("2", "Patient Number", "RFC-3881"),
    EncounterNumber("3", "Encounter Number", "RFC-3881"),
    EnrolleeNumber("4", "Enrollee Number", "RFC-3881"),
    SocialSecurityNumber("5", "Social Security Number", "RFC-3881"),
    AccountNumber("6", "Account Number", "RFC-3881"),
    GuarantorNumber("7", "Guarantor Number", "RFC-3881"),
    ReportName("8", "Report Name", "RFC-3881"),
    ReportNumber("9", "Report Number", "RFC-3881"),
    SearchCriteria("10", "Search Criteria", "RFC-3881"),
    UserIdentifier("11", "User Identifier", "RFC-3881"),
    URI("12", "URI", "RFC-3881"),

    // DCM
    StudyInstanceUID("110180", "Study Instance UID", CODE_SYSTEM_NAME_DCM),
    SOPClassUID("110181", "SOP Class UID", CODE_SYSTEM_NAME_DCM),
    NodeID("110182", "Node ID", CODE_SYSTEM_NAME_DCM),

    // IHE
    XdsMetadata("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd", "submission set classificationNode", "IHE XDS Metadata"),

    /**
     * Id correlating the audit records that the two ends of a transaction write about it, as
     * <a href="https://profiles.ihe.net/ITI/BALP/index.html">IHE BALP</a> defines it: the contents of an
     * {@code X-Request-Id} header. The fallback for a correlation id from any header without a code of
     * its own.
     */
    XRequestId("XrequestId", "X-Request-Id", "IHE BALP"),

    // w3c, https://www.w3.org/TR/trace-context/

    /**
     * A correlation id propagated as <a href="https://www.w3.org/TR/trace-context/">W3C Trace
     * Context</a>, i.e. the contents of a {@code traceparent} header.
     */
    W3cTraceContext("traceparent", "traceparent", "w3c"),

    // e-health-suisse

    /**
     * The same {@code traceparent} as {@link #W3cTraceContext}, under the code system name the Swiss EPR
     * claimed for it. Trace context itself is a W3C standard and nothing Swiss, so this is only for
     * deployments bound to that convention -- everything else records a {@code traceparent} as
     * {@link #W3cTraceContext}.
     */
    SwissW3cTraceContext("traceparent", "traceparent", "e-health-suisse"),

    // openzipkin B3, https://github.com/openzipkin/b3-propagation

    /**
     * A correlation id propagated in the B3 single header, which packs the trace id, the span id and the
     * sampling state into one value. Only the trace id is recorded, since the span id differs between
     * the two ends of a transaction.
     */
    B3SingleHeader("b3", "b3", "openzipkin"),

    /**
     * A correlation id propagated in the B3 multi-header flavour, whose {@code X-B3-TraceId} carries the
     * trace id on its own. The recorded value is the same as for {@link #B3SingleHeader}.
     */
    B3MultiHeader("X-B3-TraceId", "X-B3-TraceId", "openzipkin");

    @Getter
    private final ParticipantObjectIdType value;

    ParticipantObjectIdTypeCode(String code, String displayName, String codeSystemName) {
        this.value = ParticipantObjectIdType.of(code, codeSystemName, displayName);
    }

    public static ParticipantObjectIdTypeCode enumForCode(String code) {
        return EnumeratedValueSet.enumForCode(ParticipantObjectIdTypeCode.class, code);
    }
}

