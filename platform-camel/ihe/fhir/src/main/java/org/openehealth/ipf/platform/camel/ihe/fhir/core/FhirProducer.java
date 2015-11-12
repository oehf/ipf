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

package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.impl.DefaultProducer;
import org.openehealth.ipf.commons.ihe.fhir.atna.FhirAuditDataset;

/**
 * @since 3.1
 */
public abstract class FhirProducer<AuditDatasetType extends FhirAuditDataset> extends DefaultProducer {

    private IGenericClient client;

    public FhirProducer(Endpoint endpoint) {
        super(endpoint);
    }

    protected synchronized IGenericClient getClient() throws Exception {
        if (client == null) {
            FhirContext context = getEndpoint().getFhirComponentConfiguration().getContext();

            // For the producer, the path is supposed to be the server URL
            String path = getEndpoint().getInterceptableConfiguration().getPath();

            // For now, assume http as only protocol

            path = "http://" + path;
            client = context.newRestfulGenericClient(path);
        }
        return client;
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
    }

    @Override
    public FhirEndpoint<AuditDatasetType, FhirComponent<AuditDatasetType>> getEndpoint() {
        return (FhirEndpoint<AuditDatasetType, FhirComponent<AuditDatasetType>>)super.getEndpoint();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        doProcess(exchange, getClient());
    }

    protected abstract void doProcess(Exchange exchange, IGenericClient client) throws InvalidPayloadException;
}
