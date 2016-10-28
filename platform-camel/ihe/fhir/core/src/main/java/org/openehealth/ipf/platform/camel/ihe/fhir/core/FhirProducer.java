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

package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.openehealth.ipf.commons.ihe.core.SecurityInformation;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.FhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareApacheRestfulClientFactory;
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirSecurityInformation;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

import java.util.List;
import java.util.Map;

/**
 * @author Christian Ohr
 * @since 3.1
 */
public class FhirProducer<AuditDatasetType extends FhirAuditDataset> extends DefaultProducer {

    private IGenericClient client;
    private HeaderFilterStrategy headerFilterStrategy;

    public FhirProducer(Endpoint endpoint) {
        super(endpoint);
    }

    protected synchronized IGenericClient getClient(Exchange exchange) throws Exception {
        if (client == null) {
            FhirContext context = getEndpoint().getContext();
            SslAwareApacheRestfulClientFactory clientFactory = (SslAwareApacheRestfulClientFactory)context.getRestfulClientFactory();
            FhirEndpointConfiguration<AuditDatasetType> config = getEndpoint().getInterceptableConfiguration();
            FhirSecurityInformation securityInformation = config.getSecurityInformation();
            clientFactory.setSecurityInformation(securityInformation);

            // For the producer, the path is supposed to be the server URL
            String path = config.getPath();

            path = (config.getSecurityInformation().isSecure() ? "https://" : "http://") + path;
            client = clientFactory.newGenericClient(path);

            if (securityInformation != null && securityInformation.getUsername() != null) {
                client.registerInterceptor(new BasicAuthInterceptor(securityInformation.getUsername(), securityInformation.getPassword()));
            }

            // deploy user-defined HAPI interceptors
            List<HapiClientInterceptorFactory> factories = config.getHapiClientInterceptorFactories();
            if (factories != null) {
                for (HapiClientInterceptorFactory factory : factories) {
                    client.registerInterceptor(factory.newInstance(getEndpoint(), exchange));
                }
            }

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
                getClient(exchange),
                exchange.getIn().getBody(),
                exchange.getIn().getHeaders());
        Object result = executableClient.execute();
        Message resultMessage = Exchanges.resultMessage(exchange);
        resultMessage.setBody(result);
    }


}
