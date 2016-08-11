/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core.atna.custom;

import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.ImportEvent;
import org.openhealthtools.ihe.atna.auditor.events.ihe.QueryEvent;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.openehealth.ipf.commons.ihe.core.atna.custom.CustomAuditorUtils.configureEvent;

/**
 * Implementation of Fhir Auditors to send audit messages for
 * <ul>
 * <li>ITI-83 (PIXM Query)</li>
 * <li>ITI-78 (PDQM Query)</li>
 * <li>ITI-65, ITI-66, ITI67 (MHD)</li>
 * </ul>
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class FhirAuditor extends IHEAuditor {

    public static FhirAuditor getAuditor() {
        AuditorModuleContext ctx = AuditorModuleContext.getContext();
        return (FhirAuditor) ctx.getAuditor(FhirAuditor.class);
    }

    public void auditIti65(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String documentResponderUri,
            String clientIpAddress,
            String patientId,
            String manifestId) {
        if (!isAuditorEnabled()) {
            return;
        }
        ImportEvent importEvent = new ImportEvent(false, eventOutcome, new CustomIHETransactionEventTypeCodes.ProvideDocumentBundle(), null);
        configureEvent(this, serverSide, importEvent, null, null, documentResponderUri, documentResponderUri, clientIpAddress);

        if (!EventUtils.isEmptyOrNull(patientId)) {
            importEvent.addPatientParticipantObject(patientId);
        }

        importEvent.addSubmissionSetParticipantObject(manifestId);

        audit(importEvent);
    }

    public void auditIti66(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String documentResponderUri,
            String clientIpAddress,
            String queryString,
            Collection<String> patientIds) {
        if (!isAuditorEnabled()) {
            return;
        }

        QueryEvent event = new QueryEvent(
                true,
                eventOutcome,
                new CustomIHETransactionEventTypeCodes.DocumentManifestQuery(),
                Collections.emptyList());

        configureEvent(this, serverSide, event, null, null, documentResponderUri, documentResponderUri, clientIpAddress);
        if (!EventUtils.isEmptyOrNull(patientIds)) {
            patientIds.forEach(event::addPatientParticipantObject);
        }
        event.addQueryParticipantObject("MobileDocumentManifestQuery", null, payloadBytes(queryString), null,
                new CustomIHETransactionEventTypeCodes.DocumentManifestQuery());

        audit(event);
    }

    public void auditIti67(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String documentResponderUri,
            String clientIpAddress,
            String queryString,
            Collection<String> patientIds) {
        if (!isAuditorEnabled()) {
            return;
        }

        QueryEvent event = new QueryEvent(
                true,
                eventOutcome,
                new CustomIHETransactionEventTypeCodes.DocumentReferenceQuery(),
                Collections.emptyList());

        configureEvent(this, serverSide, event, null, null, documentResponderUri, documentResponderUri, clientIpAddress);
        if (!EventUtils.isEmptyOrNull(patientIds)) {
            patientIds.forEach(event::addPatientParticipantObject);
        }
        event.addQueryParticipantObject("MobileDocumentReferenceQuery", null, payloadBytes(queryString), null,
                new CustomIHETransactionEventTypeCodes.DocumentReferenceQuery());

        audit(event);
    }

    public void auditIti78(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String pdqSupplierUri,
            String clientIpAddress,
            String queryString,
            Collection<String> patientIds) {
        if (!isAuditorEnabled()) {
            return;
        }

        QueryEvent event = new QueryEvent(
                true,
                eventOutcome,
                new CustomIHETransactionEventTypeCodes.PDQMQuery(),
                Collections.emptyList());

        configureEvent(this, serverSide, event, null, null, pdqSupplierUri, pdqSupplierUri, clientIpAddress);
        if (!EventUtils.isEmptyOrNull(patientIds)) {
            patientIds.forEach(event::addPatientParticipantObject);
        }
        event.addQueryParticipantObject("MobilePatientDemographicsQuery", null, payloadBytes(queryString), null,
                new CustomIHETransactionEventTypeCodes.PDQMQuery());
        audit(event);
    }

    public void auditIti83(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String pixManagerUri,
            String clientIpAddress,
            String queryString,
            Collection<String> patientIds) {
        if (!isAuditorEnabled()) {
            return;
        }

        QueryEvent event = new QueryEvent(
                true,
                eventOutcome,
                new CustomIHETransactionEventTypeCodes.PIXMQuery(),
                Collections.emptyList());

        configureEvent(this, serverSide, event, null, null, pixManagerUri, pixManagerUri, clientIpAddress);
        if (!EventUtils.isEmptyOrNull(patientIds)) {
            patientIds.forEach(event::addPatientParticipantObject);
        }
        event.addQueryParticipantObject("PIXmQuery", null, payloadBytes(queryString), null,
                new CustomIHETransactionEventTypeCodes.PIXMQuery());
        audit(event);
    }


    protected static byte[] payloadBytes(String payload) {
        if (payload == null) {
            return null;
        }
        try {
            return payload.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return payload.getBytes(Charset.defaultCharset());
        }
    }


}
