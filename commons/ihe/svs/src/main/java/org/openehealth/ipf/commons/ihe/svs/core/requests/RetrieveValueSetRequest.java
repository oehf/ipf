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

import javax.xml.bind.annotation.*;

/**
 * Model of an SVS RetrieveValueSetRequest
 *
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveValueSetRequestType", propOrder = { "valueSet" })
@XmlRootElement(name = "RetrieveValueSetRequest")
@Data
public class RetrieveValueSetRequest {

    @XmlElement(name = "ValueSet", required = true)
    @NonNull
    private ValueSetRequest valueSet;

    /**
     * Empty constructor for JAXB.
     */
    public RetrieveValueSetRequest() {
    }

    public RetrieveValueSetRequest(@NonNull final ValueSetRequest valueSet) {
        this.valueSet = valueSet;
    }
}
