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
package org.openehealth.ipf.commons.audit.event;


import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;

import java.util.List;

/**
 * Builds an Audit Event representing a Patient Record event as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.3.4
 * <p>
 * This message describes the event of a patient record being created, modified, accessed, or deleted.
 *
 * @author Christian Ohr
 */
public class PatientRecordBuilder extends BaseAuditMessageBuilder<PatientRecordBuilder> {

    public PatientRecordBuilder(EventOutcomeIndicator outcome, EventActionCode action, EventType eventType, PurposeOfUse... purposesOfUse) {
        super();
        setEventIdentification(outcome,
                action,
                EventIdCode.PatientRecord,
                eventType,
                purposesOfUse
        );
    }

    /**
     * @param userId               The identity of the person or process manipulating the data. If both are known, then two active
     *                             participants shall be included (both the person and the process).
     * @param altUserId            The Active Participant's Alternate UserID
     * @param userName             The Active Participant's UserName
     * @param networkAccessPointId The Active Participant's Network Access Point ID
     * @param roleIds              The Active Participant's Role ids
     * @param userIsRequestor      A single user (either local or remote) shall be identified as the requestor, i.e.,
     *                             UserIsRequestor with a value of TRUE. This accommodates both push and pull transfer models for media
     * @return this
     */
    public PatientRecordBuilder addUserParticipant(String userId,
                                String altUserId,
                                String userName,
                                String networkAccessPointId,
                                List<ActiveParticipantRoleId> roleIds,
                                boolean userIsRequestor) {
        return addActiveParticipant(userId, altUserId, userName, userIsRequestor, roleIds,
                networkAccessPointId);
    }

    public PatientRecordBuilder addPatient(String patientId, String patientName, List<TypeValuePairType> details) {
        return addPatientParticipantObject(patientId, patientName, details, null);
    }

    public PatientRecordBuilder addPatient(String patientId, String patientName, List<TypeValuePairType> details, ParticipantObjectDataLifeCycle lifeCycle) {
        return addPatientParticipantObject(patientId, patientName, details, lifeCycle);
    }

    @Override
    public void validate() {
        super.validate();
        int aps = getMessage().getActiveParticipants().size();
        if (aps < 1 || aps > 2) {
            throw new AuditException("Must have one or two user ActiveParticipants");
        }
        if (getMessage().findParticipantObjectIdentifications(poi -> poi.getParticipantObjectIDTypeCode() == ParticipantObjectIdTypeCode.PatientNumber).size() != 1) {
            throw new AuditException("Must one ParticipantObjectIdentification with ParticipantObjectIDTypeCode PatientNumber");
        }
    }
}
