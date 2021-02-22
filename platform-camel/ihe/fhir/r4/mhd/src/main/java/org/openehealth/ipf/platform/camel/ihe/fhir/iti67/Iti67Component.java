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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti67;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67Options;
import org.openehealth.ipf.commons.ihe.fhir.iti67.Iti67OptionsProvider;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponentWithOptions;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MHD.QueryDocumentReferenceInteractions.ITI_67;

/**
 * Component for MHD Retrieve Document Reference (ITI-67)
 *
 * @author Christian Ohr
 * @since 3.6
 */
public class Iti67Component extends FhirComponentWithOptions<FhirQueryAuditDataset, Iti67Options, Iti67OptionsProvider> {

    public Iti67Component() {
        super(ITI_67, Iti67OptionsProvider::new);
    }

    public Iti67Component(CamelContext context) {
        super(context, ITI_67, Iti67OptionsProvider::new);
    }

    @Override
    protected Iti67Endpoint doCreateEndpoint(String uri, FhirEndpointConfiguration<FhirQueryAuditDataset> config) {
        return new Iti67Endpoint(uri, this, config);
    }

}
