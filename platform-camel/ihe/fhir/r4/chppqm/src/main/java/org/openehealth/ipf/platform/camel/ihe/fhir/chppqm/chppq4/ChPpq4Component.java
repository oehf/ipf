/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.chppqm.chppq4;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

import static org.openehealth.ipf.commons.ihe.fhir.chppqm.CH_PPQM.SubmitInteractions.CH_PPQ_4;

public class ChPpq4Component extends FhirComponent<ChPpqmAuditDataset> {

    public ChPpq4Component() {
        super(CH_PPQ_4);
    }

    public ChPpq4Component(CamelContext context) {
        super(context, CH_PPQ_4);
    }

    @Override
    protected ChPpq4Endpoint doCreateEndpoint(String uri, FhirEndpointConfiguration<ChPpqmAuditDataset> config) {
        return new ChPpq4Endpoint(uri, this, config);
    }

}
