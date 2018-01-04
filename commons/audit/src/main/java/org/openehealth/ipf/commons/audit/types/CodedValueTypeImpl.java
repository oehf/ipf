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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode
class CodedValueTypeImpl implements CodedValueType {

    @Getter
    private final String code;

    @Getter
    private final String originalText;

    @Getter
    private final String codeSystemName;

    @Getter @Setter
    private String displayName;

    CodedValueTypeImpl(String code, String codeSystemName, String originalText) {
        this.code = requireNonNull(code, "code of CodedValueType must be not null");
        this.codeSystemName = requireNonNull(codeSystemName, "codeSystemName of CodedValueType must be not null");
        this.originalText = requireNonNull(originalText, "originalText of CodedValueType must be not null");
    }

}
