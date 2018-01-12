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
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;

import java.util.Collections;

/**
 * Builds an Audit Event representing a Security Alert event as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.3.11
 * <p>
 * This message describes any event for which a node needs to report a security alert,
 * e.g., a node authentication failure when establishing a secure communications channel.
 * </p>
 * <p>
 * The Node Authentication event can be used to report both successes and failures.
 * If reporting of success is done, this could generate a very large number of audit messages,
 * since every authenticated DICOM association, HL7 transaction, and HTML connection should result
 * in a successful node authentication. It is expected that in most situations only the failures
 * will be reported.
 * </p>
 *
 * @author Christian Ohr
 */
public class SecurityAlertBuilder extends BaseAuditMessageBuilder<SecurityAlertBuilder> {

    /**
     * @param outcome   Success implies an informative alert. The other failure values imply warning codes that indicate
     *                  the severity of the alert. A Minor or Serious failure indicates that mitigation efforts were
     *                  effective in maintaining system security. A Major failure indicates that mitigation efforts may
     *                  not have been effective, and that the security system may have been compromised.
     * @param eventType event type
     */
    public SecurityAlertBuilder(EventOutcomeIndicator outcome,
                                String eventOutcomeDescription,
                                EventType eventType) {
        super();
        setEventIdentification(outcome,
                eventOutcomeDescription,
                EventActionCode.Execute,
                EventIdCode.SecurityAlert,
                eventType,
                (PurposeOfUse[]) null
        );
    }


    /**
     * @param userId          UserID
     * @param altUserId       Alternate UserID
     * @param userName        UserName
     * @param networkId       Network Access Point ID
     * @param userIsRequestor Whether the destination participant represents the requestor (i.e. pull request)
     * @return this
     */
    public SecurityAlertBuilder addReportingActiveParticipant(String userId,
                                                              String altUserId,
                                                              String userName,
                                                              ActiveParticipantRoleId roleId,
                                                              String networkId,
                                                              boolean userIsRequestor) {
        return addActiveParticipant(userId, altUserId, userName, userIsRequestor, Collections.singletonList(roleId), networkId);
    }

    /**
     * @param userId    UserID
     * @param altUserId Alternate UserID
     * @param userName  UserName
     * @param networkId Network Access Point ID
     * @return this
     */
    public SecurityAlertBuilder addPerformingActiveParticipant(String userId,
                                                               String altUserId,
                                                               String userName,
                                                               ActiveParticipantRoleId roleId,
                                                               String networkId) {
        return addActiveParticipant(userId, altUserId, userName, false, Collections.singletonList(roleId), networkId);
    }

    /**
     * @param node   the identity of the node that is the subject of the alert either in the form ofnode_name@domain_nameor as an IP address
     * @param role   {@link ParticipantObjectTypeCodeRole#MasterFile} or {@link ParticipantObjectTypeCodeRole#SecurityResource}
     * @param reason free text description of the nature of the alert as the value
     * @return this
     */
    public SecurityAlertBuilder addAlertNodeSubjectParticipantObject(String node,
                                                                     ParticipantObjectTypeCodeRole role,
                                                                     String reason) {
        return addParticipantObjectIdentification(ParticipantObjectIdTypeCode.NodeID,
                null, null,
                reason != null ?
                        Collections.singletonList(getTypeValuePair("Alert Description", reason)) :
                        Collections.emptyList(),
                node,
                ParticipantObjectTypeCode.System,
                role,
                null,
                null);
    }

    /**
     * @param uri    the URI of the file or other resource that is the subject of the alert
     * @param role   {@link ParticipantObjectTypeCodeRole#MasterFile} or {@link ParticipantObjectTypeCodeRole#SecurityResource}
     * @param reason free text description of the nature of the alert as the value
     * @return this
     */
    public SecurityAlertBuilder addAlertUriSubjectParticipantObject(String uri,
                                                                    ParticipantObjectTypeCodeRole role,
                                                                    String reason) {
        return addParticipantObjectIdentification(ParticipantObjectIdTypeCode.URI,
                null, null,
                reason != null ?
                        Collections.singletonList(getTypeValuePair("Alert Description", reason)) :
                        Collections.emptyList(),
                uri,
                ParticipantObjectTypeCode.System,
                role,
                null,
                null);
    }

    @Override
    public void validate() {
        super.validate();
        int aps = getMessage().getActiveParticipants().size();
        if (aps < 1 || aps > 2) {
            throw new AuditException("Must have one or two ActiveParticipants reporting this event");
        }
    }
}
