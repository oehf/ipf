/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti119;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti78.Iti78QueryResourceClientRequestFactory;

/**
 * Standard Configuration for Iti119Component. Supports lazy-loading by default.
 *
 * @author Christian Ohr
 * @since 5.0
 */
public class Iti119TransactionConfiguration extends FhirTransactionConfiguration<FhirQueryAuditDataset> {

    public Iti119TransactionConfiguration() {
        super("pdqm-iti119",
                "Patient Demographics Match",
                true,
                new Iti119ClientAuditStrategy(),
                new Iti119ServerAuditStrategy(),
                FhirVersionEnum.R4,
                new Iti119ResourceProvider(),                    // Consumer side. accept patient searches
                new Iti119ClientRequestFactory(),
                FhirTransactionValidator.NO_VALIDATION);
        setSupportsLazyLoading(true);
    }
}
