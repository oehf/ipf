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
import org.openehealth.ipf.commons.audit.types.EnumeratedValueSet;

/**
 * Audit Active Participant Role ID Code as specified in
 * <a href="https://dicom.nema.org/medical/dicom/current/output/html/part16.html#sect_CID_402">Part 16, CID 402</a>
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

    Application("110150", CODE_SYSTEM_NAME_DCM, "Application"),
    ApplicationLauncher("110151", CODE_SYSTEM_NAME_DCM, "Application Launcher"),
    Destination("110152", CODE_SYSTEM_NAME_DCM, "Destination Role ID"),
    Source("110153", CODE_SYSTEM_NAME_DCM, "Source Role ID"),
    DestinationMedia("110154", CODE_SYSTEM_NAME_DCM, "Destination Media"),
    SourceMedia("110155", CODE_SYSTEM_NAME_DCM, "Source Media"),
    Initiator("110156", CODE_SYSTEM_NAME_DCM, "Initiator Role ID"),
    Person("125676002", CODE_SYSTEM_NAME_SCT, "Person");

    @Getter
    private final ActiveParticipantRoleId value;

    ActiveParticipantRoleIdCode(String code, String codeSystem, String displayName) {
        this.value = ActiveParticipantRoleId.of(code, codeSystem, displayName);
    }

    public static ActiveParticipantRoleIdCode enumForCode(String code) {
        return EnumeratedValueSet.enumForCode(ActiveParticipantRoleIdCode.class, code);
    }

}