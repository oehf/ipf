/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xacml20.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;

import static org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants.CodingSystemIds;

/**
 * @author Dmytro Rud
 */
@AllArgsConstructor
@XmlType(name = "SubjectRole", namespace = "http://www.openehealth.org/ipf/ppq")
@XmlEnum
public enum SubjectRole {
    PATIENT       (new CE("PAT",  CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Patient")),
    PROFESSIONAL  (new CE("HCP",  CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Healthcare Professional")),
    ASSISTANT     (new CE("ASS",  CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Assistant")),
    REPRESENTATIVE(new CE("REP",  CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Representative")),
    DOCUMENT_ADMIN(new CE("DADM", CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Document Administrator")),
    POLICY_ADMIN  (new CE("PADM", CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Policy Administrator")),
    TECHNICAL_USER(new CE("TCU",  CodingSystemIds.SWISS_SUBJECT_ROLE, "eHealth Suisse EPD Akteure", "Technical User")),
    ;

    @Getter private final CE code;

    public static SubjectRole fromCode(CE code) {
        for (var subjectRole : values()) {
            if (subjectRole.code.equals(code)) {
                return subjectRole;
            }
        }
        return null;
    }
}
