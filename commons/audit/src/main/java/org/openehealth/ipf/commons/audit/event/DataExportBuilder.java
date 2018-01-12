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
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.MediaType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;

import java.util.Collections;

import static java.util.Objects.requireNonNull;

/**
 * Builds an Audit Event representing a Data Export event as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.3.4
 * <p>
 * This message describes the event of exporting data from a system, meaning that the data
 * is leaving control of the system's security domain. Examples of exporting include printing to paper,
 * recording on film, conversion to another format for storage in an EHR, writing to removable media,
 * or sending via e-mail. Multiple patients may be described in one event message.
 * </p>
 *
 * @author Christian Ohr
 */
public class DataExportBuilder extends BaseAuditMessageBuilder<DataExportBuilder> {

    public DataExportBuilder(EventOutcomeIndicator outcome, EventType eventType, PurposeOfUse... purposesOfUse) {
        this(outcome, null, eventType, purposesOfUse);
    }

    public DataExportBuilder(EventOutcomeIndicator outcome, String eventOutcomeDescription, EventType eventType, PurposeOfUse... purposesOfUse) {
        this(outcome, eventOutcomeDescription, EventActionCode.Read, eventType, purposesOfUse);
    }

    public DataExportBuilder(EventOutcomeIndicator outcome, String eventOutcomeDescription, EventActionCode eventActionCode, EventType eventType, PurposeOfUse... purposesOfUse) {
        super();
        setEventIdentification(outcome,
                eventOutcomeDescription,
                eventActionCode,
                EventIdCode.Export,
                eventType,
                purposesOfUse
        );
    }

    /**
     * @param userId               The identity of the remote user or process receiving the data
     * @param altUserId            Alternate UserID
     * @param userName             UserName
     * @param networkAccessPointId Network Access Point ID
     * @param userIsRequestor      A single user (either local or remote) shall be identified as the requestor, i.e.,
     *                             UserIsRequestor with a value of TRUE. This accommodates both push and pull transfer models for media
     * @return this
     */
    public DataExportBuilder addReceivingParticipant(String userId, String altUserId, String userName,
                                                     String networkAccessPointId, boolean userIsRequestor) {
        return addDestinationActiveParticipant(userId, altUserId, userName, networkAccessPointId, userIsRequestor);
    }

    /**
     * @param userId               The identity of the local user or process exporting the data. If both are known,
     *                             then two active participants shall be included (both the person and the process).
     * @param altUserId            Alternate UserID
     * @param userName             UserName
     * @param networkAccessPointId Network Access Point ID
     * @param userIsRequestor      A single user (either local or remote) shall be identified as the requestor, i.e.,
     *                             UserIsRequestor with a value of TRUE. This accommodates both push and pull transfer models for media
     * @return this
     */
    public DataExportBuilder addExportingParticipant(String userId, String altUserId, String userName,
                                                     String networkAccessPointId, boolean userIsRequestor) {
        return addSourceActiveParticipant(userId, altUserId, userName, networkAccessPointId, userIsRequestor);
    }

    /**
     * @param userId               UserID
     * @param altUserId            Alternate UserID
     * @param userName             UserName
     * @param networkAccessPointId Network Access Point ID
     * @param mediaIdentifier      Media Identifier
     * @param mediaType            Media Type
     * @return this
     */
    public DataExportBuilder setDestinationMediaParticipant(String userId, String altUserId, String userName,
                                                            String networkAccessPointId,
                                                            NetworkAccessPointTypeCode networkAccessPointTypeCode,
                                                            String mediaIdentifier,
                                                            MediaType mediaType) {
        return addActiveParticipant(
                userId,
                altUserId,
                userName,
                false,
                Collections.singletonList(ActiveParticipantRoleIdCode.DestinationMedia),
                networkAccessPointId,
                networkAccessPointTypeCode,
                mediaIdentifier,
                requireNonNull(mediaType));
    }

    @Override
    public void validate() {
        super.validate();
        int sources = getMessage().findActiveParticipants(ap -> ap.getRoleIDCodes().contains(ActiveParticipantRoleIdCode.Source)).size();
        if (sources < 1 || sources > 2) {
            throw new AuditException("Must have one or two ActiveParticipant with RoleIDCode Source");
        }
        if (getMessage().findActiveParticipants(ap -> ap.getRoleIDCodes().contains(ActiveParticipantRoleIdCode.DestinationMedia)).size() != 1) {
            throw new AuditException("Must have one ActiveParticipant with RoleIDCode DestinationMedia");
        }
        if (getMessage().findParticipantObjectIdentifications(poi -> poi.getParticipantObjectIDTypeCode() == ParticipantObjectIdTypeCode.PatientNumber).isEmpty()) {
            throw new AuditException("Must one or more ParticipantObjectIdentification with ParticipantObjectIDTypeCode PatientNumber");
        }
    }
}
