/*
 * Copyright 2021 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.svs.core.responses;

import lombok.Data;
import lombok.NonNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Model of an SVS Concept
 *
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CD")
@Data
public class Concept {

    @XmlAttribute(name = "code", required = true)
    @NonNull
    private String code;

    @XmlAttribute(name = "codeSystem", required = true)
    @NonNull
    private String codeSystem;

    @XmlAttribute(name = "displayName", required = true)
    @NonNull
    private String displayName;

    @XmlAttribute(name = "codeSystemVersion")
    private String codeSystemVersion;

    /**
     * Empty constructor for JAXB.
     */
    public Concept() {
    }

    public Concept(@NonNull final String code,
                   @NonNull final String codeSystem,
                   @NonNull final String displayName,
                   final String codeSystemVersion) {
        this.code = code;
        this.codeSystem = codeSystem;
        this.displayName = displayName;
        this.codeSystemVersion = codeSystemVersion;
    }
}
