/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.pharm5;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;

/**
 * Static configuration for CMPD PHARM-5 transaction components
 *
 * @author Quentin Ligier
 * @since 4.3
 **/
public class Pharm5TransactionConfiguration extends FhirTransactionConfiguration<FhirQueryAuditDataset> {

    public Pharm5TransactionConfiguration() {
        super("cmpd-pharm5",
                "PHARM-5",
                true,
                new Pharm5AuditStrategy(false),
                new Pharm5AuditStrategy(true),
                FhirVersionEnum.R4,
                new Pharm5ResourceProvider(), // Consumer side. accept registrations
                new Pharm5ClientRequestFactory(),
                FhirTransactionValidator.NO_VALIDATION);
        setSupportsLazyLoading(false);
    }
}
