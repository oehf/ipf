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
import org.openehealth.ipf.commons.audit.types.AuditSource;
import org.openehealth.ipf.commons.audit.types.CodedValueType;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;

/**
 * Audit Source Type Codes as originally specified in https://tools.ietf.org/html/rfc3881#section-5.4
 * and now maintained in http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.1.2
 * This value set is a literal part of the audit schema, ie.e. no other codes may be used.
 *
 * @author Christian Ohr
 */
public enum AuditSourceType implements EnumeratedCodedValue<AuditSource>, AuditSource {

    EndUserInterface("1", "End-user interface"),
    DataAcquisitionDevice("2", "Data acquisition device or instrument"),
    WebServerProcess("3", "Web Server Process or Thread"),
    ApplicationServerProcess("4", "Application Server Process or Thread"),
    DatabaseServerProcess("5", "Database Server Process or Thread"),
    SecurityServer("6", "Security Server"),
    NetworkComponent("7", "Network Component"),
    OperatingSoftware("8", "Operating Software"),
    Other("9", "Other");

    @Getter
    private AuditSource value;

    AuditSourceType(String code, String displayName) {
        this.value = AuditSource.of(code, "DCM", displayName);
    }

}
