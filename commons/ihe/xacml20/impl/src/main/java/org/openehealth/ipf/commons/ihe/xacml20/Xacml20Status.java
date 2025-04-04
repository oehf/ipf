/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.xacml20;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Xacml20Status {

    SUCCESS("urn:oasis:names:tc:SAML:2.0:status:Success"),
    REQUESTER_ERROR("urn:oasis:names:tc:SAML:2.0:status:Requester"),
    RESPONDER_ERROR("urn:oasis:names:tc:SAML:2.0:status:Responder"),
    VERSION_MISMATCH("urn:oasis:names:tc:SAML:2.0:status:VersionMismatch"),

    // only for CH:ADR
    EPR_NOT_HOLDER("urn:e-health-suisse:2015:error:not-holder-of-patient-policies"),
    ;

    @Getter
    private final String code;

    public static Xacml20Status fromCode(String code) {
        for (Xacml20Status status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

}
