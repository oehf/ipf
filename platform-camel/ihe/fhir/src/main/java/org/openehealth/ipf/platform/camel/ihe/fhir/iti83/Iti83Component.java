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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti83;

import ca.uhn.fhir.context.FhirContext;
import org.apache.camel.CamelContext;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.*;

/**
 * Component for PIXm (ITI-83)
 */
public class Iti83Component extends FhirComponent<Iti83AuditDataset> {

    public static final FhirComponentConfiguration<Iti83AuditDataset> CONFIGURATION =
            new FhirComponentConfiguration<>(
                    FhirContext.forDstu2Hl7Org(),
                    new Iti83ResourceProvider());

    private static final FhirAuditStrategy<Iti83AuditDataset> CLIENT_AUDIT_STRATEGY = new Iti83ClientAuditStrategy();
    private static final FhirAuditStrategy<Iti83AuditDataset> SERVER_AUDIT_STRATEGY = new Iti83ServerAuditStrategy();

    public Iti83Component() {
        super();
    }

    public Iti83Component(CamelContext context) {
        super(context);
    }


    @Override
    public FhirComponentConfiguration<Iti83AuditDataset> getFhirComponentConfiguration() {
        return CONFIGURATION;
    }

    @Override
    protected Iti83Endpoint doCreateEndpoint(String uri, FhirEndpointConfiguration<Iti83AuditDataset> config) {
        return new Iti83Endpoint(uri, this, config);
    }

    @Override
    public FhirAuditStrategy<Iti83AuditDataset> getServerAuditStrategy() {
        return SERVER_AUDIT_STRATEGY;
    }

    @Override
    public FhirAuditStrategy<Iti83AuditDataset> getClientAuditStrategy() {
        return CLIENT_AUDIT_STRATEGY;
    }
}
