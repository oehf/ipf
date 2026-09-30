/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.audit.events;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.event.PatientRecordEventBuilder;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;

import java.util.Collections;

import static org.openehealth.ipf.commons.ihe.fhir.audit.events.JwtUtils.addJwtParticipant;

/**
 * Builder for DICOM Patient Record events of FHIR transactions that create, update or delete a Patient
 * resource, such as the PIXm Patient Identity Feed. Other than for the HL7v2-based transactions, the
 * patient ID is expected as FHIR token ({@code system|value}), and the participants of an access token
 * are added as well.
 * <p>
 * The patient the event is about is added first, so that it is not mistaken for the patient an
 * access token may name.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class BalpPatientRecordBuilder extends PatientRecordEventBuilder<BalpPatientRecordBuilder> {

    public BalpPatientRecordBuilder(AuditContext auditContext,
                                    FhirAuditDataset auditDataset,
                                    EventActionCode action,
                                    EventType eventType,
                                    String patientId,
                                    PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, action, eventType, purposesOfUse);
        addPatients(null, null, patientId != null ? patientId : auditContext.getAuditValueIfMissing());
        addJwtParticipant(delegate, auditDataset, auditContext);
    }

    /**
     * Adds the Patient resource that has been created, updated or deleted. Its role follows the
     * action, as the BALP RESTful patterns expect it: {@link ParticipantObjectTypeCodeRole#Job} for
     * a create, {@link ParticipantObjectTypeCodeRole#Report} for an update or delete.
     *
     * @param resourceReference reference of the resource, e.g. {@code Patient/123}. Ignored if null.
     * @return this
     */
    public BalpPatientRecordBuilder addDataEntity(String resourceReference) {
        if (resourceReference != null) {
            var action = getMessage().getEventIdentification().getEventActionCode();
            delegate.addParticipantObjectIdentification(
                ParticipantObjectIdTypeCode.URI,
                null,
                null,
                Collections.emptyList(),
                resourceReference,
                ParticipantObjectTypeCode.System,
                action == EventActionCode.Create ?
                    ParticipantObjectTypeCodeRole.Job :
                    ParticipantObjectTypeCodeRole.Report,
                null,
                null);
        }
        return self();
    }

    /**
     * FHIR transactions record the patient ID as FHIR token, {@code system|value}.
     *
     * @param patientId patient ID
     */
    @Override
    protected void validatePatientId(String patientId) {
        var separator = patientId.indexOf('|');
        if (separator < 0 || separator == patientId.length() - 1) {
            throw new AuditException("Patient ID should be a FHIR token system|value, but was " + patientId);
        }
    }
}
