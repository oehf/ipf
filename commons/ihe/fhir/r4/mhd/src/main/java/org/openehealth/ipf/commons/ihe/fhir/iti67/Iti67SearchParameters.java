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

package org.openehealth.ipf.commons.ihe.fhir.iti67;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchAndSortParameters;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.*;
import static org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67ResourceProvider.STU3_INDEXED;

/**
 * @author Christian Ohr
 * @since 3.6
 */
@Builder
@ToString
@AllArgsConstructor
public class Iti67SearchParameters extends FhirSearchAndSortParameters<DocumentReference> {

    @Getter @Setter private ReferenceParam patientReference;
    @Getter @Setter private TokenParam patientIdentifier;
    @Getter @Setter private DateRangeParam date;
    @Getter @Setter private StringParam authorFamilyName;
    @Getter @Setter private StringParam authorGivenName;
    @Getter @Setter private TokenParam authorIdentifier;
    @Getter @Setter private TokenParam identifier;
    @Getter @Setter private TokenOrListParam status;
    @Getter @Setter private TokenOrListParam category;
    @Getter @Setter private TokenOrListParam type;
    @Getter @Setter private TokenOrListParam setting;
    @Getter @Setter private DateRangeParam period;
    @Getter @Setter private TokenOrListParam facility;
    @Getter @Setter private TokenOrListParam event;
    @Getter @Setter private TokenOrListParam securityLabel;
    @Getter @Setter private TokenOrListParam format;

    @Getter @Setter private ReferenceOrListParam related;
    @Getter @Setter private TokenOrListParam relatedId;
    @Getter @Setter private TokenParam _id;

    @Getter @Setter private SortSpec sortSpec;
    @Getter @Setter private Set<Include> includeSpec;

    @Getter private final FhirContext fhirContext;


    @Override
    public List<TokenParam> getPatientIdParam() {
        if (_id != null)
            return Collections.singletonList(_id);
        if (patientReference != null)
            return Collections.singletonList(patientReference.toTokenParam(fhirContext));

        return Collections.singletonList(patientIdentifier);
    }

    public Iti67SearchParameters setAuthor(ReferenceAndListParam author) {
        if (author != null) {
            author.getValuesAsQueryTokens().forEach(param -> {
                var ref = param.getValuesAsQueryTokens().get(0);
                var authorChain = ref.getChain();
                if (Practitioner.SP_FAMILY.equals(authorChain)) {
                    setAuthorFamilyName(ref.toStringParam(getFhirContext()));
                } else if (Practitioner.SP_GIVEN.equals(authorChain)) {
                    setAuthorGivenName(ref.toStringParam(getFhirContext()));
                } else if (Practitioner.SP_IDENTIFIER.equals(authorChain)) {
                    setAuthorIdentifier(ref.toTokenParam(getFhirContext()));
                }
            });
        }
        return this;
    }

    @Override
    public Optional<Comparator<DocumentReference>> comparatorFor(String paramName) {
        if (DocumentReference.SP_DATE.equals(paramName) || STU3_INDEXED.equals(paramName)) {
            return Optional.of(CP_DATE);
        } else if (DocumentReference.SP_AUTHOR.equals(paramName)) {
            return Optional.of(CP_AUTHOR);
        }
        return Optional.empty();
    }

    private static final Comparator<DocumentReference> CP_DATE = comparing(
        DocumentReference::getDate, nullsLast(naturalOrder()));

    private static final Comparator<DocumentReference> CP_AUTHOR = comparing(
        Iti67SearchParameters::getAuthorName, nullsLast(naturalOrder()));

    private static String getAuthorName(DocumentReference documentReference) {
        if (!documentReference.hasAuthor()) return null;
        var author = documentReference.getAuthorFirstRep();
        if (author.getResource() instanceof PractitionerRole practitionerRole) {
            if (!practitionerRole.hasPractitioner()) return null;
            author = practitionerRole.getPractitioner();
        }
        if (author.getResource() == null) return null;
        if (author.getResource() instanceof Practitioner practitioner) {
            if (!practitioner.hasName()) return null;
            var name = practitioner.getNameFirstRep();
            return name.getFamilyElement().getValueNotNull() + name.getGivenAsSingleString();
        }
        if (author.getResource() instanceof Patient patient) {
            if (!patient.hasName()) return null;
            var name = patient.getNameFirstRep();
            return name.getFamilyElement().getValueNotNull() + name.getGivenAsSingleString();
        }
        return null;
    }

}
