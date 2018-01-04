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
import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;

import java.util.Collections;

/**
 * Builds an Audit Event representing a Query event as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.3.10
 * <p>
 * This message describes the event of a Query being issued or received.
 * The message does NOT record the response to the query, but merely records the fact
 * that a query was issued.
 *
 * @author Christian Ohr
 */
public class QueryBuilder extends BaseAuditMessageBuilder<QueryBuilder> {

    public QueryBuilder(EventOutcomeIndicator outcome, EventType eventType, PurposeOfUse... purposesOfUse) {
        super();
        setEventIdentification(outcome,
                EventActionCode.Execute,
                EventIdCode.Query,
                eventType,
                purposesOfUse
        );
    }

    /**
     * Process Issuing the Query
     *
     * @param userId               The Active Participant's UserID
     * @param altUserId            The Active Participant's Alternate UserID
     * @param userName             The Active Participant's UserName
     * @param networkAccessPointId The Active Participant's Network Access Point ID
     * @param userIsRequestor      A single user (either local or remote) shall be identified as the requestor, i.e.,
     *                             UserIsRequestor with a value of TRUE. This accommodates both push and pull transfer models for media
     * @return this
     */
    public QueryBuilder setQueryingParticipant(String userId, String altUserId, String userName,
                                               String networkAccessPointId, boolean userIsRequestor) {
        return addSourceActiveParticipant(userId, altUserId, userName, networkAccessPointId, userIsRequestor);
    }

    /**
     * The process that will respond to the query
     *
     * @param userId               The Active Participant's UserID
     * @param altUserId            The Active Participant's Alternate UserID
     * @param userName             The Active Participant's UserName
     * @param networkAccessPointId The Active Participant's Network Access Point ID
     * @param userIsRequestor      A single user (either local or remote) shall be identified as the requestor, i.e.,
     *                             UserIsRequestor with a value of TRUE. This accommodates both push and pull transfer models for media
     * @return this
     */
    public QueryBuilder setRespondingParticipant(String userId, String altUserId, String userName,
                                                 String networkAccessPointId, boolean userIsRequestor) {
        return addDestinationActiveParticipant(userId, altUserId, userName, networkAccessPointId, userIsRequestor);
    }


    /**
     * @param userId          The Active Participant's UserID
     * @param altUserId       The Active Participant's Alternate UserID
     * @param userName        The Active Participant's UserName
     * @param networkId       The Active Participant's Network Access Point ID
     * @param userIsRequestor Whether the destination participant represents the requestor (i.e. pull request)
     * @return this
     */
    public QueryBuilder addOtherActiveParticipant(String userId,
                                                  String altUserId,
                                                  String userName,
                                                  ActiveParticipantRoleId roleId,
                                                  String networkId,
                                                  boolean userIsRequestor) {
        return addActiveParticipant(userId, altUserId, userName, userIsRequestor, Collections.singletonList(roleId), networkId);
    }

    @Override
    public void validate() {
        super.validate();
        if (getMessage().findActiveParticipants(ap -> ap.getRoleIDCodes().contains(ActiveParticipantRoleIdCode.Source)).size() != 1) {
            throw new AuditException("Must have one ActiveParticipant with RoleIDCode Source");
        }
        if (getMessage().findActiveParticipants(ap -> ap.getRoleIDCodes().contains(ActiveParticipantRoleIdCode.Destination)).size() != 1) {
            throw new AuditException("Must have one ActiveParticipant with RoleIDCode Destination");
        }

        // DICOM restricts Participating Object to be one SOP Queried and the Query, but IHE also uses this Audit Event
        // for all sorts of other queries, so we cannot validate this at this point.
    }
}
