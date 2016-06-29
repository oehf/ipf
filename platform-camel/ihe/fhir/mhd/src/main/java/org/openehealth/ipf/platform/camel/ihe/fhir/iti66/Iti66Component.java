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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti66;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti66.Iti66ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti66.Iti66ServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponentConfiguration;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

/**
 * Component for MHD Retrieve Document Manifest (ITI-66)
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class Iti66Component extends FhirComponent<FhirQueryAuditDataset> {

    private static final FhirComponentConfiguration DEFAULT_CONFIGURATION = new Iti66Configuration();

    private static final AuditStrategy<FhirQueryAuditDataset> CLIENT_AUDIT_STRATEGY = new Iti66ClientAuditStrategy();
    private static final AuditStrategy<FhirQueryAuditDataset> SERVER_AUDIT_STRATEGY = new Iti66ServerAuditStrategy();

    public Iti66Component() {
        super(DEFAULT_CONFIGURATION);
    }

    public Iti66Component(CamelContext context) {
        super(context);
        setFhirComponentConfiguration(DEFAULT_CONFIGURATION);
    }

    public Iti66Component(FhirComponentConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected Iti66Endpoint doCreateEndpoint(String uri, FhirEndpointConfiguration<FhirQueryAuditDataset> config) {
        return new Iti66Endpoint(uri, this, config);
    }

    @Override
    public AuditStrategy<FhirQueryAuditDataset> getServerAuditStrategy() {
        return SERVER_AUDIT_STRATEGY;
    }

    @Override
    public AuditStrategy<FhirQueryAuditDataset> getClientAuditStrategy() {
        return CLIENT_AUDIT_STRATEGY;
    }
}
