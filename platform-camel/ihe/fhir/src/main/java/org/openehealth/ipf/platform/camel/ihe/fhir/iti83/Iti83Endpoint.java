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

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.spi.UriEndpoint;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpointConfiguration;

/**
 * PIXM Query endpoint (ITI-83)
 */
@UriEndpoint(scheme = "pixm-iti83", title = "ITI-83 PIXm", syntax = "pixm-iti83:host:port", consumerClass = Iti83Consumer.class, label = "http")
public class Iti83Endpoint extends FhirEndpoint<Iti83AuditDataset, Iti83Component> {

    public Iti83Endpoint(String uri, Iti83Component fhirComponent, FhirEndpointConfiguration config) {
        super(uri, fhirComponent, config);
    }

    @Override
    public Consumer doCreateConsumer(Processor processor) {
        return new Iti83Consumer(this, processor);
    }

    @Override
    protected String createEndpointUri() {
        return "pixm-iti83:" + "not-imlpemented yet";
    }
}
