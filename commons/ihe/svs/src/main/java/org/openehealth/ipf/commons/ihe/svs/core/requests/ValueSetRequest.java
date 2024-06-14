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

package org.openehealth.ipf.commons.ihe.svs.core.requests;

import lombok.Data;
import lombok.NonNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Model of an SVS ValueSetRequest
 *
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValueSetRequestType")
@Data
public class ValueSetRequest {

    @XmlAttribute(name = "id", required = true)
    @NonNull
    private String id;

    @XmlAttribute(name = "lang", namespace = javax.xml.XMLConstants.XML_NS_URI)
    private String lang;

    @XmlAttribute(name = "version")
    private String version;

    /**
     * Empty constructor for JAXB.
     */
    public ValueSetRequest() {
    }

    public ValueSetRequest(@NonNull final String id,
                           final String lang,
                           final String version) {
        this.id = id;
        this.lang = lang;
        this.version = version;
    }
}
