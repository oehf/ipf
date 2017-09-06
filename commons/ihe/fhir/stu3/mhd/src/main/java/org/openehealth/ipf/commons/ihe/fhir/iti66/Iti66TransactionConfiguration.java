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
import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;

/**
 * Standard Configuration for Iti66Component. Supports lazy-loading by default.
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class Iti66TransactionConfiguration extends FhirTransactionConfiguration {

    public Iti66TransactionConfiguration() {
        super("mhd-iti66",
                "Find Document Manifests",
                true,
                new Iti66AuditStrategy(false),
                new Iti66AuditStrategy(true),
                FhirContext.forDstu3(),
                new Iti66ResourceProvider(),                    // Consumer side. accept registrations
                new Iti66ClientRequestFactory(),
                FhirTransactionValidator.NO_VALIDATION);        // Formulate requests
        setSupportsLazyLoading(true);
    }
}
