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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti65;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.fhir.iti65.Iti65AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

import static org.openehealth.ipf.commons.ihe.fhir.MHD.Interactions.ITI_65;

/**
 * Component for MHD Register Document (ITI-65)
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class Iti65Component extends FhirComponent<Iti65AuditDataset> {


    public Iti65Component() {
        super(ITI_65);
    }

    public Iti65Component(CamelContext context) {
        super(context, ITI_65);
    }

    @Override
    protected Iti65Endpoint doCreateEndpoint(String uri, FhirEndpointConfiguration<Iti65AuditDataset> config) {
        return new Iti65Endpoint(uri, this, config);
    }


}
