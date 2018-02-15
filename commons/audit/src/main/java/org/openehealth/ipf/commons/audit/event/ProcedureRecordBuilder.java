/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.audit.event;

import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;

import java.util.Collections;

/**
 * Builds an Audit Event representing a Procedure Record event as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.3.15
 * <p>
 * This message describes the event of a procedure record being created, accessed, modified,
 * accessed, or deleted. This message may only include information about a single patient.
 * </p>
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class ProcedureRecordBuilder extends BaseAuditMessageBuilder<ProcedureRecordBuilder> {

    public ProcedureRecordBuilder(EventOutcomeIndicator outcome,
                                  String eventOutcomeDescription,
                                  EventActionCode eventActionCode,
                                  EventType eventType,
                                  PurposeOfUse... purposesOfUse) {
        super();
        setEventIdentification(outcome,
                eventOutcomeDescription,
                eventActionCode,
                EventIdCode.OrderRecord,
                eventType,
                purposesOfUse
        );
    }

    /**
     * @param patientId   patient ID
     * @param patientName patient name
     * @return this
     */
    public ProcedureRecordBuilder setPatientParticipantObject(String patientId, String patientName) {
        if (patientId != null) {
            addPatientParticipantObject(patientId, patientName, Collections.emptyList(), null);
        }
        return self();
    }

    @Override
    public void validate() {
        super.validate();
        int participants = getMessage().getActiveParticipants().size();
        if (participants < 1 || participants > 2) {
            throw new AuditException("Must have one or two ActiveParticipants");
        }
        if (getMessage().findParticipantObjectIdentifications(poi -> poi.getParticipantObjectIDTypeCode() == ParticipantObjectIdTypeCode.PatientNumber).size() != 1) {
            throw new AuditException("Must one ParticipantObjectIdentification with ParticipantObjectIDTypeCode PatientNumber");
        }
    }
}
