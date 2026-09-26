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

import org.apache.camel.Category;
import org.apache.camel.spi.UriEndpoint;
import org.openehealth.ipf.commons.ihe.fhir.iti104.Iti104AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

/**
 * PIXm Patient Identity Feed FHIR endpoint (ITI-104)
 *
 * @author Christian Ohr
 * @since 6.0
 */
@UriEndpoint(scheme = "pixm-iti104", title = "ITI-104 PIXm Feed", syntax = "pixm-iti104:host:port", category = Category.HTTP)
public class Iti104Endpoint extends FhirEndpoint<Iti104AuditDataset> {

    public Iti104Endpoint(String uri, Iti104Component fhirComponent, FhirEndpointConfiguration config) {
        super(uri, fhirComponent, config);
    }

    @Override
    protected String createEndpointUri() {
        return "pixm-iti104:" + "not-implemented yet";
    }
}
