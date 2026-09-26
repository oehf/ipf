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

import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmProfile;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PixmProfile;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PixmValidator;

import java.util.Set;

/**
 * Standard Configuration for Iti104Component (Patient Identity Feed FHIR).
 * <p>
 * A Patient that claims to conform to the PIXm or PDQm Patient profile is validated against it,
 * any other Patient against the base FHIR Patient.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class Iti104TransactionConfiguration extends FhirTransactionConfiguration {

    private static final String BASE_PATIENT_PROFILE = "http://hl7.org/fhir/StructureDefinition/Patient";

    public Iti104TransactionConfiguration() {
        super(
            "pixm-iti104",
            "Patient Identity Feed FHIR",
            false,
            new Iti104AuditStrategy(false),
            new Iti104AuditStrategy(true),
            FhirVersionEnum.R4,
            new Iti104ResourceProvider(),       // Consumer side. accept conditional update and delete
            new Iti104ClientRequestFactory(),
            PixmValidator::new);
        setRequestValidationProfiles(Set.of(
            PixmProfile.PIXM_PATIENT_PROFILE,
            PdqmProfile.PDQM_PATIENT_PROFILE,
            BASE_PATIENT_PROFILE
        ));
        setSupportsLazyLoading(false);
    }
}
