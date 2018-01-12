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
 * Builds an Audit Event representing a Network Entry event as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.3.9
 * <p>
 * This message describes the event of a system, such as a mobile device, intentionally entering or leaving the network.
 * </p>
 *
 * @author Christian Ohr
 */
public class NetworkEntryBuilder extends BaseAuditMessageBuilder<NetworkEntryBuilder> {

    public NetworkEntryBuilder(EventOutcomeIndicator outcome,
                               String eventOutcomeDescription,
                               EventTypeCode eventTypeCode) {
        super();
        setEventIdentification(outcome,
                eventOutcomeDescription,
                EventActionCode.Read,
                EventIdCode.NetworkEntry,
                eventTypeCode,
                (PurposeOfUse[]) null
        );
    }

    /**
     * Sets the Active Participant of the Node or System entering or leaving the network
     *
     * @param userId    The person or process accessing the audit trail. If both are known,
     *                  then two active participants shall be included (both the person and the process).
     * @param altUserId Alternate UserID
     * @param userName  UserName
     * @param networkId Network Access Point ID
     */
    public NetworkEntryBuilder setSystemParticipant(String userId,
                                                    String altUserId,
                                                    String userName,
                                                    ActiveParticipantRoleIdCode roleId,
                                                    String networkId) {
        return addActiveParticipant(
                userId,
                altUserId,
                userName,
                false,
                Collections.singletonList(roleId),
                networkId);
    }

    public static class EnteringNetwork extends NetworkEntryBuilder {

        public EnteringNetwork(EventOutcomeIndicator outcome) {
            this(outcome, null);
        }

        public EnteringNetwork(EventOutcomeIndicator outcome, String eventOutcomeDescription) {
            super(outcome, eventOutcomeDescription, EventTypeCode.Attach);
        }
    }

    public static class LeavingNetwork extends NetworkEntryBuilder {

        public LeavingNetwork(EventOutcomeIndicator outcome) {
            this(outcome, null);
        }

        public LeavingNetwork(EventOutcomeIndicator outcome, String eventOutcomeDescription) {
            super(outcome, eventOutcomeDescription, EventTypeCode.Detach);
        }
    }

    @Override
    public void validate() {
        super.validate();
        if (getMessage().getActiveParticipants().size() != 1) {
            throw new AuditException("Must have one ActiveParticipant");
        }
    }
}
