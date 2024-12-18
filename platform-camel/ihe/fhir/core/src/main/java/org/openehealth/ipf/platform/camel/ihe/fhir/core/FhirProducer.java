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

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.producer.HapiClientAuditInterceptor;

import java.util.List;
import java.util.Map;

/**
 * @author Christian Ohr
 * @since 3.1
 */
public class FhirProducer<AuditDatasetType extends FhirAuditDataset> extends DefaultProducer {

    public FhirProducer(Endpoint endpoint) {
        super(endpoint);
    }

    protected IGenericClient getClient(Exchange exchange) {
        var context = getEndpoint().getContext();
        var config = getEndpoint().getInterceptableConfiguration();
        var securityInformation = config.getSecurityInformation();

        // For the producer, the path is supposed to be the server URL
        var path = config.getPath();
        path = (securityInformation != null && securityInformation.isSecure() ? "https://" : "http://") + path;

        var client = context.getRestfulClientFactory().newGenericClient(path);

        if (securityInformation != null && securityInformation.getUsername() != null) {
            client.registerInterceptor(new BasicAuthInterceptor(securityInformation.getUsername(), securityInformation.getPassword()));
        }

        if (config.isAudit()) {
            var auditDataset = exchange.getIn().getHeader(Constants.FHIR_AUDIT_HEADER, FhirAuditDataset.class);
            if (auditDataset != null) {
                client.registerInterceptor(new HapiClientAuditInterceptor(auditDataset));
                exchange.getIn().removeHeader(Constants.FHIR_AUDIT_HEADER);
            }
        }

        // deploy user-defined HAPI interceptors
        var factories = config.getHapiClientInterceptorFactories();
        if (factories != null) {
            for (var factory : factories) {
                client.registerInterceptor(factory.newInstance(getEndpoint(), exchange));
            }
        }

        return client;
    }

    @Override
    public FhirEndpoint<AuditDatasetType, FhirComponent<AuditDatasetType>> getEndpoint() {
        return (FhirEndpoint<AuditDatasetType, FhirComponent<AuditDatasetType>>) super.getEndpoint();
    }

    /**
     * Processes the exchange. Body and Headers are forwarded to
     * {@link ClientRequestFactory#getClientExecutable(IGenericClient, Object, Map)}, so that
     * the actual query can be dynamically constructed from the exchange.
     *
     * @param exchange Camel exchange
     */
    @Override
    public void process(Exchange exchange) {
        var requestFactory = getEndpoint().getClientRequestFactory();
        IClientExecutable<?, ?> executableClient = requestFactory.getClientExecutable(
                getClient(exchange),
                exchange.getIn().getBody(),
                exchange.getIn().getHeaders());

        var httpHeadersObject = exchange.getIn().getHeader(Constants.HTTP_OUTGOING_HEADERS);
        if (httpHeadersObject instanceof Map) {
            var headers = (Map<String, List<String>>) httpHeadersObject;
            for (var entry : headers.entrySet()) {
                for (var value : entry.getValue()) {
                    executableClient.withAdditionalHeader(entry.getKey(), value);
                }
            }
        }

        var result = executableClient.execute();
        var resultMessage = exchange.getMessage();
        resultMessage.setBody(result);
    }


}
