/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti104;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hl7.fhir.r4.model.Identifier;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Parameters of the conditional update or delete of the Patient Identity Feed FHIR [ITI-104]
 * transaction, i.e. the identifier of the patient to be added, revised, merged or removed. The
 * Patient Identifier Cross-reference Manager hands them into the route in the
 * {@link org.openehealth.ipf.commons.ihe.fhir.Constants#FHIR_REQUEST_PARAMETERS} header.
 *
 * @author Christian Ohr
 * @since 6.0
 */
@AllArgsConstructor
public class Iti104Parameters implements FhirSearchParameters {

    @Getter
    private final TokenParam identifier;

    @Getter
    private final FhirContext fhirContext;

    /**
     * @return the patient identifier as {@link Identifier}
     */
    public Identifier getPatientIdentifier() {
        return new Identifier()
            .setSystem(identifier.getSystem())
            .setValue(identifier.getValue());
    }

    @Override
    public Set<Include> getIncludeSpec() {
        return Collections.emptySet();
    }

    @Override
    public List<TokenParam> getPatientIdParam() {
        return List.of(identifier);
    }
}
