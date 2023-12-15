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

package org.openehealth.ipf.commons.ihe.fhir.iti66;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.*;
import lombok.*;
import org.hl7.fhir.r4.model.DocumentManifest;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchAndSortParameters;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

/**
 * @since 3.6
 */
@Builder
@ToString
@AllArgsConstructor
public class Iti66ListSearchParameters extends FhirSearchAndSortParameters<DocumentManifest> {

    @Getter @Setter private DateRangeParam date;
    @Getter @Setter private StringParam authorFamilyName;
    @Getter @Setter private StringParam authorGivenName;
    @Getter @Setter private TokenOrListParam code;
    @Getter @Setter private TokenOrListParam designationType;
    @Getter @Setter private TokenOrListParam sourceId;
    @Getter @Setter private TokenOrListParam status;
    @Getter @Setter private TokenParam identifier;
    @Getter @Setter private ReferenceParam patientReference;
    @Getter @Setter private TokenParam patientIdentifier;
    @Getter @Setter private TokenParam _id;

    @Getter @Setter private SortSpec sortSpec;
    @Getter @Setter private Set<Include> includeSpec;

    @Getter
    private final FhirContext fhirContext;

    @Override
    public List<TokenParam> getPatientIdParam() {
        if (_id != null)
            return Collections.singletonList(_id);
        if (patientReference != null)
            return Collections.singletonList(patientReference.toTokenParam(fhirContext));

        return Collections.singletonList(patientIdentifier);
    }

    public Iti66ListSearchParameters setAuthor(ReferenceAndListParam author) {
        if (author != null) {
            author.getValuesAsQueryTokens().forEach(param -> {
                var ref = param.getValuesAsQueryTokens().get(0);
                var authorChain = ref.getChain();
                if (Practitioner.SP_FAMILY.equals(authorChain)) {
                    setAuthorFamilyName(ref.toStringParam(getFhirContext()));
                } else if (Practitioner.SP_GIVEN.equals(authorChain)) {
                    setAuthorGivenName(ref.toStringParam(getFhirContext()));
                }
            });
        }
        return this;
    }

    @Override
    public Optional<Comparator<DocumentManifest>> comparatorFor(String paramName) {
        if (DocumentManifest.SP_CREATED.equals(paramName)) {
            return Optional.of(CP_CREATED);
        } else if (DocumentManifest.SP_AUTHOR.equals(paramName)) {
            return Optional.of(CP_AUTHOR);
        }
        return Optional.empty();
    }

    private static final Comparator<DocumentManifest> CP_CREATED = nullsLast(comparing(DocumentManifest::getCreated));

    private static final Comparator<DocumentManifest> CP_AUTHOR = nullsLast(comparing(documentManifest -> {
        if (!documentManifest.hasAuthor()) return null;
        var author = documentManifest.getAuthorFirstRep();
        if (author.getResource() instanceof PractitionerRole) {
            var practitionerRole = (PractitionerRole) author.getResource();
            if (!practitionerRole.hasPractitioner()) return null;
            author = practitionerRole.getPractitioner();
        }
        if (author.getResource() == null) return null;
        if (author.getResource() instanceof Practitioner) {
            var practitioner = (Practitioner) author.getResource();
            if (!practitioner.hasName()) return null;
            var name = practitioner.getNameFirstRep();
            return name.getFamilyElement().getValueNotNull() + name.getGivenAsSingleString();
        }
        return null;
    }));
}
