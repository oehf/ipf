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
package org.openehealth.ipf.commons.audit.types;

import java.io.Serializable;

/**
 * @author Christian Ohr
 * @since 3.5
 */
public interface CodedValueType extends Serializable {

    String CODE_SYSTEM_NAME_IHE_TRANSACTIONS = "IHE Transactions";
    String CODE_SYSTEM_NAME_DCM = "DCM";
    String CODE_SYSTEM_NAME_EHS = "e-health-suisse";

    String getCode();

    String getOriginalText();

    String getCodeSystemName();

    String getDisplayName();

    static CodedValueType of(String code, String codeSystemName, String originalText) {
        return of(code, codeSystemName, originalText, null);
    }

    static CodedValueType of(String code, String codeSystemName, String originalText, String displayName) {
        var c = new CodedValueTypeImpl(code, codeSystemName, originalText);
        c.setDisplayName(displayName);
        return c;
    }
}
