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

import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdValidator;

import java.util.Arrays;
import java.util.Set;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.ITI66_FIND_DOCUMENT_LISTS_RESPONSE_BUNDLE_PROFILE;

/**
 * Standard Configuration for Iti66Component. Supports lazy-loading by default.
 *
 * @author Christian Ohr
 * @since 3.6
 */
public class Iti66TransactionConfiguration extends FhirTransactionConfiguration<FhirQueryAuditDataset> {

    public Iti66TransactionConfiguration() {
        super("mhd-iti66",
                "Find Document Manifests or Lists",
                true,
                new Iti66AuditStrategy(false),
                new Iti66AuditStrategy(true),
                FhirVersionEnum.R4,
                Arrays.asList(new Iti66ListResourceProvider(), new Iti66DocumentManifestResourceProvider()),
                new Iti66ClientRequestFactory(),
                MhdValidator::new);
        setSupportsLazyLoading(true);
        setResponseValidationProfiles(Set.of(ITI66_FIND_DOCUMENT_LISTS_RESPONSE_BUNDLE_PROFILE));
    }
}
