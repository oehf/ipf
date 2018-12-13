/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.pcc44;

import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

/**
 * @author Christian Ohr
 * @since 3.5
 */
public class Pcc44Endpoint extends FhirEndpoint<FhirQueryAuditDataset, Pcc44Component> {

    public Pcc44Endpoint(String uri, Pcc44Component fhirComponent, FhirEndpointConfiguration<FhirQueryAuditDataset> config) {
        super(uri, fhirComponent, config);
    }

    @Override
    protected String createEndpointUri() {
        return "qedm-pcc44:" + "not-implemented yet";
    }
}
