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
import org.openehealth.ipf.commons.ihe.svs.core.jaxbadapters.OffsetDateTimeAdapter;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.OffsetDateTime;

/**
 * Model of an SVS RetrieveValueSetResponse
 *
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveValueSetResponseType", propOrder = {"valueSet"})
@XmlRootElement(name = "RetrieveValueSetResponse")
@Data
public class RetrieveValueSetResponse {

    @XmlElement(name = "ValueSet", required = true)
    @NonNull
    private ValueSetResponse valueSet;

    @XmlAttribute(name = "cacheExpirationHint")
    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    protected OffsetDateTime cacheExpirationHint;

    /**
     * Empty constructor for JAXB.
     */
    public RetrieveValueSetResponse() {
    }

    public RetrieveValueSetResponse(@NonNull final ValueSetResponse valueSet,
                                    final OffsetDateTime cacheExpirationHint) {
        this.valueSet = valueSet;
        this.cacheExpirationHint = cacheExpirationHint;
    }
}