/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.pcc44;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

/**
 * @author Christian Ohr
 * @since 3.5
 */
@ToString
public class ObservationSearchParameters extends Pcc44CommonSearchParameters {

    @Getter
    private DateRangeParam date;

    @Getter
    private TokenAndListParam category;

    @Getter
    private TokenOrListParam code;

    @Builder
    ObservationSearchParameters(ReferenceParam patientReference,
                                       DateRangeParam date,
                                       TokenAndListParam category,
                                       TokenOrListParam code,
                                       TokenParam _id,
                                       SortSpec sortSpec,
                                       Set<Include> includeSpec,
                                       Set<Include> revIncludeSpec,
                                       FhirContext fhirContext) {
        super(patientReference, _id, sortSpec, includeSpec, revIncludeSpec, fhirContext);
        this.date = date;
        this.category = category;
        this.code = code;
    }
}
