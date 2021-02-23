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
package org.openehealth.ipf.commons.ihe.fhir.iti65;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.BatchTransactionClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.BatchTransactionResourceProvider;
import org.openehealth.ipf.commons.ihe.fhir.BundleProfileSelector;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;

/**
 * Standard Configuration for Iti65Component.
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class Iti65TransactionConfiguration extends FhirTransactionConfiguration<Iti65AuditDataset> {

    public Iti65TransactionConfiguration() {
        super("mhd-iti65",
                "Provide Document Bundle",
                false,
                new Iti65ClientAuditStrategy(),
                new Iti65ServerAuditStrategy(),
                FhirVersionEnum.DSTU3,
                BatchTransactionResourceProvider.getInstance(),      // Consumer side. accept registrations
                BatchTransactionClientRequestFactory.getInstance(),  // Formulate requests
                Iti65Validator::new);
        setStaticConsumerSelector(new BundleProfileSelector(Iti65Constants.ITI65_PROFILE));
    }

    @Override
    public void setSupportsLazyLoading(boolean supportsLazyLoading) {
        if (supportsLazyLoading)
            throw new IllegalArgumentException("Lazy loading is not applicable for ITI-65");
    }
}
