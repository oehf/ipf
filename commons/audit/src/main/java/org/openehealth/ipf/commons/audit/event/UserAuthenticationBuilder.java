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
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;

import java.util.Collections;

/**
 * Builds an User Authentication representing a Network Entry event as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.3.12
 * <p>
 * This message describes the event that a user has attempted to log on or log off.
 * This report can be made regardless of whether the attempt was successful or not.
 * No Participant Objects are needed for this message.
 * </p>
 * <p>
 * The user usually has UserIsRequestor TRUE, but in the case of a logout timer,
 * the Node might be the UserIsRequestor.
 * </p>
 *
 * @author Christian Ohr
 */
public class UserAuthenticationBuilder extends BaseAuditMessageBuilder<UserAuthenticationBuilder> {

    public UserAuthenticationBuilder(EventOutcomeIndicator outcome,
                                     String eventOutcomeDescription,
                                     EventTypeCode eventTypeCode,
                                     PurposeOfUse... purposesOfUse) {
        super();
        setEventIdentification(outcome,
                eventOutcomeDescription,
                EventActionCode.Execute,
                EventIdCode.UserAuthentication,
                eventTypeCode,
                purposesOfUse
        );
    }

    public UserAuthenticationBuilder setAuthenticatedParticipant(String userId, String networkId) {
        return setAuthenticatedParticipant(userId, null, null, true, null, networkId);
    }

    /**
     * Sets the Active Participant of the Node or System entering or leaving the network
     *
     * @param userId    The person or process accessing the audit trail. If both are known,
     *                  then two active participants shall be included (both the person and the process).
     * @param altUserId The Active Participant's Alternate UserID
     * @param userName  The Active Participant's UserName
     * @param networkId The Active Participant's Network Access Point ID
     */
    public UserAuthenticationBuilder setAuthenticatedParticipant(String userId,
                                                                 String altUserId,
                                                                 String userName,
                                                                 boolean userIsRequestor,
                                                                 ActiveParticipantRoleIdCode roleId,
                                                                 String networkId) {
        return addActiveParticipant(
                userId,
                altUserId,
                userName,
                userIsRequestor,
                roleId != null ? Collections.singletonList(roleId) : Collections.emptyList(),
                networkId);
    }

    public UserAuthenticationBuilder setAuthenticatingSystemParticipant(String userId, String networkId) {
        return setAuthenticatingSystemParticipant(userId, null, null, true, null, networkId);
    }

    /**
     * Node or System performing authentication
     *
     * @param userId    The Active Participant's UserID
     * @param altUserId The Active Participant's Alternate UserID
     * @param userName  The Active Participant's UserName
     * @param networkId The Active Participant's Network Access Point ID
     */
    public UserAuthenticationBuilder setAuthenticatingSystemParticipant(String userId,
                                                                        String altUserId,
                                                                        String userName,
                                                                        boolean userIsRequestor,
                                                                        ActiveParticipantRoleIdCode roleId,
                                                                        String networkId) {
        return addActiveParticipant(
                userId,
                altUserId,
                userName,
                userIsRequestor,
                Collections.singletonList(roleId),
                networkId);
    }

    public static class Login extends UserAuthenticationBuilder {

        public Login(EventOutcomeIndicator outcome, PurposeOfUse... purposeOfUse) {
            this(outcome, null, purposeOfUse);
        }

        public Login(EventOutcomeIndicator outcome, String eventOutcomeDescription, PurposeOfUse... purposeOfUse) {
            super(outcome, eventOutcomeDescription, EventTypeCode.Login, purposeOfUse);
        }
    }

    public static class Logout extends UserAuthenticationBuilder {

        public Logout(EventOutcomeIndicator outcome, PurposeOfUse... purposeOfUse) {
            this(outcome, null, purposeOfUse);
        }

        public Logout(EventOutcomeIndicator outcome, String eventOutcomeDescription, PurposeOfUse... purposeOfUse) {
            super(outcome, eventOutcomeDescription, EventTypeCode.Logout, purposeOfUse);
        }
    }

    @Override
    public void validate() {
        super.validate();
        int participants = getMessage().getActiveParticipants().size();
        if (participants < 1 || participants > 2) {
            throw new AuditException("Must have one or two ActiveParticipants");
        }
    }
}
