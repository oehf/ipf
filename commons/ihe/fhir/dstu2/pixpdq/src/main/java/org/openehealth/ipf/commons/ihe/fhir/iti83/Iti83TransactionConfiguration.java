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
package org.openehealth.ipf.commons.ihe.fhir.iti83;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;

/**
 * Standard Configuration for Iti83Component. Lazy-loading of results is by default not supported.
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class Iti83TransactionConfiguration extends FhirTransactionConfiguration<FhirQueryAuditDataset> {

    public Iti83TransactionConfiguration() {
        super(
                "pixm-iti83",
                "PIX Query For Mobile",
                true,
                new Iti83AuditStrategy(false),
                new Iti83AuditStrategy(true),
                FhirVersionEnum.DSTU2_HL7ORG,
                new Iti83ResourceProvider(),        // Consumer side. accept $ihe-pix operation
                new Iti83ClientRequestFactory(),
                FhirTransactionValidator.NO_VALIDATION);
        setSupportsLazyLoading(false);
    }

}
