/*
 * Copyright 2025 the original author or authors.
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

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of an SVS ConceptList
 *
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConceptListType", propOrder = { "concept" })
@Data
public class ConceptList {

    @XmlElement(name = "Concept", required = true)
    private final List<Concept> concept = new ArrayList<>();

    @XmlAttribute(name = "lang", namespace = javax.xml.XMLConstants.XML_NS_URI)
    private String lang;

    /**
     * Empty constructor for JAXB.
     */
    public ConceptList() {
    }

    public ConceptList(final String lang) {
        this.lang = lang;
    }
}