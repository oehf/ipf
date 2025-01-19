/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.svs.iti48;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.svs.core.audit.SvsAuditDataset;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;
import org.openehealth.ipf.commons.ihe.ws.*;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.svs.SVS.Interactions.ITI_48;

/**
 * The Camel component for the ITI-48 transaction.
 *
 * @author Quentin Ligier
 */
public class Iti48Component extends AbstractWsComponent<SvsAuditDataset, WsTransactionConfiguration<SvsAuditDataset>, WsInteractionId<WsTransactionConfiguration<SvsAuditDataset>>> {

    public Iti48Component() {
        super(ITI_48);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new AbstractWsEndpoint<>(uri, remaining, this, parameters, Iti48Service.class) {

            @Override
            public JaxWsClientFactory<SvsAuditDataset> getJaxWsClientFactory() {
                return new JaxWsRequestClientFactory<>(
                    getComponent().getWsTransactionConfiguration(),
                    getServiceUrl(),
                    isAudit() ? getClientAuditStrategy() : null,
                    getAuditContext(),
                    getCustomCxfInterceptors(),
                    getFeatures(),
                    getProperties(),
                    getCorrelator(),
                    getSecurityInformation(),
                    getHttpClientPolicy());
            }

            @Override
            public JaxWsServiceFactory<SvsAuditDataset> getJaxWsServiceFactory() {
                return new JaxWsRequestServiceFactory<>(
                    getComponent().getWsTransactionConfiguration(),
                    getServiceAddress(),
                    isAudit() ? getComponent().getServerAuditStrategy() : null,
                    getAuditContext(),
                    getCustomCxfInterceptors(),
                    getRejectionHandlingStrategy());
            }

            @Override
            public AbstractWsProducer<SvsAuditDataset, WsTransactionConfiguration<SvsAuditDataset>, RetrieveValueSetRequest, RetrieveValueSetResponse> getProducer(AbstractWsEndpoint<SvsAuditDataset, WsTransactionConfiguration<SvsAuditDataset>> endpoint, JaxWsClientFactory<SvsAuditDataset> clientFactory) {
                return new SimpleWsProducer<>(
                    endpoint, clientFactory, RetrieveValueSetRequest.class, RetrieveValueSetResponse.class
                );
            }
        };
    }
}