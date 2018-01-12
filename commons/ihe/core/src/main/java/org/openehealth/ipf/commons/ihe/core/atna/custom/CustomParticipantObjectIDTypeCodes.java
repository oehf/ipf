/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.core.atna.custom;

import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

/**
 * @author Dmytro Rud
 * @deprecated
 */
public class CustomParticipantObjectIDTypeCodes extends CodedValueType {

    public static final CodedValueType ISO21091_IDENTIFIER = new CustomParticipantObjectIDTypeCodes("99SupHPD-ISO21091", "ISO 21091 Identifier", "IHE");
    public static final CodedValueType LDAP_DN = new CustomParticipantObjectIDTypeCodes("DN", "Distinguished Name", "RFC-4517");

    private CustomParticipantObjectIDTypeCodes(String code, String originalText, String codeSystemName) {
        setCode(code);
        setOriginalText(originalText);
        setCodeSystemName(codeSystemName);
    }
}
