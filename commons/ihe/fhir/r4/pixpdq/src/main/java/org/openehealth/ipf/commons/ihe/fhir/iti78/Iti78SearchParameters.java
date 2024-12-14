/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.iti78;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchAndSortParameters;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

/**
 * @since 3.6
 */
@Builder
@ToString
@AllArgsConstructor
public class Iti78SearchParameters extends FhirSearchAndSortParameters<PdqPatient> {

    @Getter @Setter private TokenAndListParam identifiers;
    @Getter @Setter private TokenParam active;
    @Getter @Setter private StringAndListParam family;
    @Getter @Setter private StringAndListParam given;
    @Getter @Setter private DateAndListParam birthDate;
    @Getter @Setter private StringParam address;
    @Getter @Setter private StringParam city;
    @Getter @Setter private StringParam country;
    @Getter @Setter private StringParam state;
    @Getter @Setter private StringParam postalCode;
    @Getter @Setter private TokenParam gender;
    @Getter @Setter private TokenParam _id;
    @Getter @Setter private StringParam telecom;
    @Getter @Setter private StringParam mothersMaidenName;

    @Getter @Setter private SortSpec sortSpec;
    @Getter @Setter private Set<Include> includeSpec;

    @Getter
    private final FhirContext fhirContext;

    @Override
    public List<TokenParam> getPatientIdParam() {
        if (_id != null)
            return Collections.singletonList(_id);
        if (identifiers != null)
            return identifiers.getValuesAsQueryTokens().stream()
                .flatMap(tol -> tol.getValuesAsQueryTokens().stream())
                .collect(Collectors.toList());
        return Collections.emptyList();
    }

    @Override
    public Optional<Comparator<PdqPatient>> comparatorFor(String paramName) {
        return switch (paramName) {
            case PdqPatient.SP_BIRTHDATE -> Optional.of(CP_DATE);
            case PdqPatient.SP_FAMILY -> Optional.of(CP_FAMILY);
            case PdqPatient.SP_GIVEN -> Optional.of(CP_GIVEN);
            default -> Optional.empty();
        };
    }

    private static final Comparator<PdqPatient> CP_DATE = nullsLast(comparing(PdqPatient::getBirthDate));
    private static final Comparator<PdqPatient> CP_FAMILY = nullsLast(comparing(patient -> patient.getNameFirstRep().getFamily()));
    private static final Comparator<PdqPatient> CP_GIVEN = nullsLast(comparing(patient -> patient.getNameFirstRep().getGivenAsSingleString()));

}
