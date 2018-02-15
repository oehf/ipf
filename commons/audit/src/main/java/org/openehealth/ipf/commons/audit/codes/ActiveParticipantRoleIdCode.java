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

package org.openehealth.ipf.commons.audit.codes;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;

/**
 * Audit Active Participant Role ID Code as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part16.html#sect_CID_402
 * 1.2.840.10008.6.1.905
 * <p>
 * ActiveParticipantRoleIdCode identifies which object took which role in the event.
 * It also covers agents, multi-purpose entities, and multi-role entities.
 * For the purpose of the event, one primary role is chosen.
 * </p>
 * <p>
 * When describing a human user’s participation in an event, the RoleIDCode value should
 * represent the access control roles/permissions that authorized the event.
 * </p>
 *
 * @author Christian Ohr
 * @since 3.5
 */
public enum ActiveParticipantRoleIdCode implements ActiveParticipantRoleId, EnumeratedCodedValue<ActiveParticipantRoleId> {

    Application("110150", "Application"),
    ApplicationLauncher("110151", "Application Launcher"),
    Destination("110152", "Destination"),
    Source("110153", "Source"),
    DestinationMedia("110154", "Destination Media"),
    SourceMedia("110155", "Source Media");

    @Getter
    private ActiveParticipantRoleId value;

    ActiveParticipantRoleIdCode(String code, String displayName) {
        this.value = ActiveParticipantRoleId.of(code, "DCM", displayName);
    }

}