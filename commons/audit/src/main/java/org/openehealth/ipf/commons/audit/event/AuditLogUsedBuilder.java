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
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;

import java.util.Collections;
import java.util.List;

/**
 * Builds an Audit Event representing a Audit Log Used event as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.3.2
 * <p>
 * This message describes the event of a person or process reading a log of audit trail information.
 * </p>
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class AuditLogUsedBuilder extends BaseAuditMessageBuilder<AuditLogUsedBuilder> {

    public AuditLogUsedBuilder(EventOutcomeIndicator outcome) {
        this(outcome, null);
    }

    public AuditLogUsedBuilder(EventOutcomeIndicator outcome,
                               String eventOutcomeDescription) {
        super();
        setEventIdentification(outcome,
                eventOutcomeDescription,
                EventActionCode.Read,
                EventIdCode.AuditLogUsed,
                null
        );
    }

    /**
     * Adds the Active Participant of the User or System that accessed the log
     *
     * @param userId    The person or process accessing the audit trail. If both are known,
     *                  then two active participants shall be included (both the person and the process).
     * @param altUserId The Active Participant's Alternate UserID
     * @param userName  The Active Participant's UserName
     * @param networkId The Active Participant's Network Access Point ID
     */
    public AuditLogUsedBuilder addAccessingParticipant(String userId,
                                                       String altUserId,
                                                       String userName,
                                                       boolean userIsRequestor,
                                                       List<ActiveParticipantRoleId> roleIds,
                                                       String networkId) {
        return addActiveParticipant(
                userId,
                altUserId,
                userName,
                userIsRequestor,
                roleIds,
                networkId);
    }

    /**
     * Adds the Participant Object representing the audit log accessed
     *
     * @param auditLogUri The URI of the audit log that was accessed
     */
    public AuditLogUsedBuilder addAuditLogIdentity(String auditLogUri) {
        return addParticipantObjectIdentification(
                        ParticipantObjectIdTypeCode.URI,
                        "Security Audit Log",
                        null,
                        Collections.emptyList(),
                        auditLogUri,
                        ParticipantObjectTypeCode.System,
                        ParticipantObjectTypeCodeRole.SecurityResource,
                        null,
                        null);
    }

    @Override
    public void validate() {
        super.validate();
        if (getMessage().getActiveParticipants().isEmpty() || getMessage().getActiveParticipants().size() > 2) {
            throw new AuditException("Must have one or two participants that started the Application");
        }
        // ITI-81 allows multiple Audit Log Identity Participating Objects, so we disable this DICOM restriction for now
        // if (getMessage().findParticipantObjectIdentifications(poi -> ParticipantObjectIdTypeCode.URI.equals(poi.getParticipantObjectIDTypeCode())).size() != 1) {
        //    throw new AuditException("Must have exactly Audit Log Identity Participating Object ");
        // }
    }
}
