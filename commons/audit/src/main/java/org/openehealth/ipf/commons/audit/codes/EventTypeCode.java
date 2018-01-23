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
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.EventType;

/**
 * Audit Event ID Code as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part16.html#sect_CID_401
 * 1.2.840.10008.6.1.904
 *
 * @author Christian Ohr
 * @since 3.5
 */
public enum EventTypeCode implements EventType, EnumeratedCodedValue<EventType> {

    ApplicationStart("110120", "Application Start"),
    ApplicationStop("110121", "Application Stop"),
    Login("110122", "Login"),
    Logout("110123", "Logout"),
    Attach("110124", "Attach"),
    Detach("110125", "Detach"),
    NodeAuthentication("110126", "Node Authentication"),
    EmergencyOverrideStarted("110127", "Emergency Override Started"),
    NetworkConfiguration("110128", "Network Configuration"),
    SecurityConfiguration("110129", "Security Configuration"),
    HardwareConfiguration("110130", "Hardware Configuration"),
    SoftwareConfiguration("110131", "Software Configuration"),
    UseOfRestrictedFunction("110132", "Use of Restricted Function"),
    AuditRecordingStopped("110133", "Audit Recording Stopped"),
    AuditRecordingStarted("110134", "Audit Recording Started"),
    ObjectSecurityAttributesChanged("110135", "Object Security Attributes Changed"),
    SecurityRolesChanged("110136", "Security Roles Changed"),
    UserSecurityAttributesChanged("110137", "User Security Attributes Changed"),
    EmergencyOverrideStopped("110138", "Emergency Override Stopped"),
    RemoteServiceOperationStarted("110139", "Remote Service Operation Started"),
    RemoteServiceOperationStopped("110140", "Remote Service Operation Stopped"),
    LocalServiceOperationStarted("110141", "Local Service Operation Started"),
    LocalServiceOperationStopped("110142", "Local Service Operation Stopped");

    @Getter
    private EventType value;

    EventTypeCode(String code, String displayName) {
        this.value = EventType.of(code, "DCM", displayName);
    }

}
