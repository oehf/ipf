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
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

import java.util.Map;

/**
 * @since 3.1
 */
public class FhirProducer<AuditDatasetType extends FhirAuditDataset> extends DefaultProducer {

    private IGenericClient client;

    public FhirProducer(Endpoint endpoint) {
        super(endpoint);
    }

    protected synchronized IGenericClient getClient() throws Exception {
        if (client == null) {
            FhirContext context = getEndpoint().getContext();

            // For the producer, the path is supposed to be the server URL
            String path = getEndpoint().getInterceptableConfiguration().getPath();

            // For now, assume http as only protocol
            path = "http://" + path;
            client = context.newRestfulGenericClient(path);

            if (getEndpoint().getInterceptableConfiguration().getAuthUserName() != null)
            client.registerInterceptor(new BasicAuthInterceptor(
                    getEndpoint().getInterceptableConfiguration().getAuthUserName(),
                    getEndpoint().getInterceptableConfiguration().getAuthPassword()));
        }
        return client;
    }

    @Override
    public FhirEndpoint<AuditDatasetType, FhirComponent<AuditDatasetType>> getEndpoint() {
        return (FhirEndpoint<AuditDatasetType, FhirComponent<AuditDatasetType>>)super.getEndpoint();
    }

    /**
     * Processes the exchange. Body and Headers are forwarded to
     * {@link ClientRequestFactory#getClientExecutable(IGenericClient, Object, Map)}, so that
     * the actual query can be dynamically constructed from the exchange.
     *
     * @param exchange Camel exchange
     * @throws Exception
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        ClientRequestFactory<?> requestFactory = getEndpoint().getClientRequestFactory();
        IClientExecutable<?, ?> executableClient = requestFactory.getClientExecutable(
                getClient(),
                exchange.getIn().getBody(),
                exchange.getIn().getHeaders());
        Object result = executableClient.execute();
        Message resultMessage = Exchanges.resultMessage(exchange);
        resultMessage.setBody(result);
    }


}
