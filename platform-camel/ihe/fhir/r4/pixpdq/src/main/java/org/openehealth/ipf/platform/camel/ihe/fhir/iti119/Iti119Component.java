/*
 * Copyright 2015 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti119;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

import static org.openehealth.ipf.commons.ihe.fhir.pixpdq.PDQM.Interactions.ITI_119;

/**
 * Component for PDQm Match (ITI-119)
 *
 * @author Christian Ohr
 * @since 5.0
 */
public class Iti119Component extends FhirComponent<FhirQueryAuditDataset> {


    public Iti119Component() {
        super(ITI_119);
    }

    public Iti119Component(CamelContext context) {
        super(context, ITI_119);
    }

    @Override
    protected Iti119Endpoint doCreateEndpoint(String uri, FhirEndpointConfiguration<FhirQueryAuditDataset> config) {
        return new Iti119Endpoint(uri, this, config);
    }

}
