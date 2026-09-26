/*
 * Copyright 2026 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.iti104;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.fhir.iti104.Iti104AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

import static org.openehealth.ipf.commons.ihe.fhir.pixpdq.PIXM.Interactions.ITI_104;

/**
 * Component for PIXm Patient Identity Feed FHIR (ITI-104)
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class Iti104Component extends FhirComponent<Iti104AuditDataset> {

    public Iti104Component() {
        super(ITI_104);
    }

    public Iti104Component(CamelContext context) {
        super(context, ITI_104);
    }

    @Override
    protected Iti104Endpoint doCreateEndpoint(String uri, FhirEndpointConfiguration config) {
        return new Iti104Endpoint(uri, this, config);
    }

}
