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
import lombok.NonNull;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model of an SVS ValueSetResponse
 *
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValueSetResponseType", propOrder = { "conceptList" })
@Data
public class ValueSetResponse {

    @XmlElement(name = "ConceptList", required = true)
    private final List<ConceptList> conceptList = new ArrayList<>();

    @XmlAttribute(name = "id", required = true)
    @NonNull
    private String id;

    @XmlAttribute(name = "displayName", required = true)
    @NonNull
    private String displayName;

    @XmlAttribute(name = "version")
    private String version;

    /**
     * Empty constructor for JAXB.
     */
    public ValueSetResponse() {
    }

    public ValueSetResponse(@NonNull final String id,
                            @NonNull final String displayName,
                            final String version) {
        this.id = id;
        this.displayName = displayName;
        this.version = version;
    }
}