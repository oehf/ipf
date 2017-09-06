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
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

import static org.openehealth.ipf.commons.ihe.fhir.MHD.Interactions.ITI_67;

/**
 * Component for MHD Retrieve Document Reference (ITI-67)
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class Iti67Component extends FhirComponent<FhirQueryAuditDataset> {


    public Iti67Component() {
        super(ITI_67);
    }

    public Iti67Component(CamelContext context) {
        super(context, ITI_67);
    }

    @Override
    protected Iti67Endpoint doCreateEndpoint(String uri, FhirEndpointConfiguration<FhirQueryAuditDataset> config) {
        return new Iti67Endpoint(uri, this, config);
    }

}
