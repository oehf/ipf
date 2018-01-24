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
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;

import java.util.Collections;


/**
 * Builds an Audit Event representing a Application Activity event as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.3.1
 * <p>
 * This audit message describes the event of an Application Entity starting or stopping.
 * This is closely related to the more general case of any kind of application startup or shutdown,
 * and may be suitable for those purposes also.
 * </p>
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class ApplicationActivityBuilder<T extends ApplicationActivityBuilder<T>> extends BaseAuditMessageBuilder<T> {

    public ApplicationActivityBuilder(EventOutcomeIndicator outcome,
                                      EventType type) {
        this(outcome, null, type);
    }

    public ApplicationActivityBuilder(EventOutcomeIndicator outcome,
                                      String eventOutcomeDescription,
                                      EventType type) {
        super();
        setEventIdentification(outcome,
                eventOutcomeDescription,
                EventActionCode.Execute,
                EventIdCode.ApplicationActivity,
                type,
                (PurposeOfUse[]) null
        );
    }

    /**
     * Add an Application Starter Active Participant to this message. This participant describes persons or processes
     * that started or stopped the application.
     *
     * @param userId The person or process starting or stopping the Application
     * @return this
     */
    public T addApplicationStarterParticipant(String userId) {
        return addApplicationStarterParticipant(userId, null, null, null);
    }

    /**
     * Add an Application Starter Active Participant to this message. This participant describes persons or processes
     * that started or stopped the application.
     *
     * @param userId    The person or process starting or stopping the Application
     * @param altUserId The Active Participant's Alternate UserID
     * @param userName  The Active Participant's UserName
     * @param networkId The Active Participant's Network Access Point ID
     * @return this
     */
    public T addApplicationStarterParticipant(String userId, String altUserId, String userName, String networkId) {
        return addActiveParticipant(
                        userId,
                        altUserId,
                        userName,
                        true,
                        Collections.singletonList(ActiveParticipantRoleIdCode.ApplicationLauncher),
                        networkId);
    }

    /**
     * Add an Application Participant to this message. This participant describes the application that is started or stopped
     * and must be added only once.
     *
     * @param userId    The identity of the process started or stopped formatted as specified in A.5.2.1.
     * @param altUserId If the process supports DICOM, then the AE Titles as specified in A.5.2.2.
     * @param userName  The Active Participant's UserName
     * @param networkId The Active Participant's Network Access Point ID
     * @return this
     */
    public T setApplicationParticipant(String userId, String altUserId, String userName, String networkId) {
        return addActiveParticipant(
                userId,
                altUserId,
                userName,
                false,
                Collections.singletonList(ActiveParticipantRoleIdCode.Application),
                networkId);
    }

    @Override
    public void validate() {
        super.validate();
        if (getMessage().findActiveParticipants(ap -> ActiveParticipantRoleIdCode.Application.equals(ap)).size() != 1) {
            throw new AuditException("Must have exactly one Application Active Participant");
        }
    }

    public static class ApplicationStart extends ApplicationActivityBuilder<ApplicationStart> {

        public ApplicationStart(EventOutcomeIndicator outcome) {
            this(outcome, null);
        }

        public ApplicationStart(EventOutcomeIndicator outcome, String eventOutcomeDescription) {
            super(outcome, eventOutcomeDescription, EventTypeCode.ApplicationStart);
        }
    }

    public static class ApplicationStop extends ApplicationActivityBuilder<ApplicationStop> {

        public ApplicationStop(EventOutcomeIndicator outcome) {
            this(outcome, null);
        }

        public ApplicationStop(EventOutcomeIndicator outcome, String eventOutcomeDescription) {
            super(outcome, eventOutcomeDescription, EventTypeCode.ApplicationStop);
        }
    }


}
