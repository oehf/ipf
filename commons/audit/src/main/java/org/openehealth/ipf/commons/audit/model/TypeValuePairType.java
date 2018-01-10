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

package org.openehealth.ipf.commons.audit.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode
public class TypeValuePairType implements Serializable {

    @Getter
    private final String type;

    @Getter
    private final byte[] value;

    public TypeValuePairType(String type, String value) {
        this(type, value.getBytes(StandardCharsets.UTF_8));
    }

    public TypeValuePairType(String type, byte[] value) {
        this.type = requireNonNull(type, "Type of TypeValuePairType must be not null");
        this.value = Base64.getEncoder().encode(requireNonNull(value, "Value of TypeValuePairType must be not null"));
    }

}
