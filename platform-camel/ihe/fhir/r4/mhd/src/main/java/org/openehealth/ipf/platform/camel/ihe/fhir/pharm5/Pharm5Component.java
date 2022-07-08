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
package org.openehealth.ipf.platform.camel.ihe.fhir.pharm5;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.CMPD.QueryPharmacyDocumentsInteractions.PHARM_5;

/**
 * Component for MHD-based, CMPD Query Pharmacy Documents over MHD (PHARM-5)
 *
 * @author Quentin Ligier
 * @since 4.3
 **/
public class Pharm5Component extends FhirComponent<FhirQueryAuditDataset> {

    public Pharm5Component() {
        super(PHARM_5);
    }

    public Pharm5Component(final CamelContext context) {
        super(context, PHARM_5);
    }

    @Override
    protected Pharm5Endpoint doCreateEndpoint(final String uri,
                                              final FhirEndpointConfiguration<FhirQueryAuditDataset> config) {
        return new Pharm5Endpoint(uri, this, config);
    }
}
