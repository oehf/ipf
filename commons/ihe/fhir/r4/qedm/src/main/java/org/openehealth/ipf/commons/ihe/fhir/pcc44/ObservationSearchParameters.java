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
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Period;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

/**
 * @author Christian Ohr
 * @since 3.6
 */
@ToString
public class ObservationSearchParameters extends Pcc44CommonSearchParameters<Observation> {

    @Getter
    private final DateRangeParam date;

    @Getter
    private final TokenAndListParam category;

    @Getter
    private final TokenOrListParam code;

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

    @Override
    protected Optional<Comparator<Observation>> comparatorFor(String paramName) {
        if (Observation.SP_DATE.equals(paramName)) {
            return Optional.of(nullsLast(CP_EFFECTIVE));
        }
        return Optional.empty();
    }

    private static final Comparator<Observation> CP_EFFECTIVE = nullsLast(comparing(observation -> {
        if (!observation.hasEffective()) return null;
        var effective = observation.getEffective();
        if (effective instanceof DateTimeType) {
            return observation.getEffectiveDateTimeType().getValueAsString();
        } else if (effective instanceof Period){
            return observation.getEffectivePeriod().getStartElement().getValueAsString();
        } else if (effective instanceof InstantType){
            return observation.getEffectiveInstantType().getValueAsString();
        } else  {
            return null;
        }
    }));
}
