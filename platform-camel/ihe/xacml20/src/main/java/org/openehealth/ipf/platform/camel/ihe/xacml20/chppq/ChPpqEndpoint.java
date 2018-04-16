/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq;

import org.openehealth.ipf.commons.ihe.ws.*;
import org.openehealth.ipf.commons.ihe.xacml20.chppq.ChPpqAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class ChPpqEndpoint extends AbstractWsEndpoint<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>> {

    public ChPpqEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>, ? extends WsInteractionId> component,
            Map<String, Object> parameters,
            Class<? extends AbstractWebService> serviceClass)
    {
        super(endpointUri, address, component, parameters, serviceClass);
    }

    @Override
    public JaxWsClientFactory<ChPpqAuditDataset> getJaxWsClientFactory() {
        return new JaxWsRequestClientFactory<>(
                getComponent().getWsTransactionConfiguration(),
                getServiceUrl(),
                isAudit() ? getClientAuditStrategy() : null,
                getAuditContext(),
                getCustomInterceptors(),
                getFeatures(),
                getProperties(),
                getCorrelator());
    }

    @Override
    public JaxWsServiceFactory<ChPpqAuditDataset> getJaxWsServiceFactory() {
        return new JaxWsRequestServiceFactory<>(
                getComponent().getWsTransactionConfiguration(),
                getServiceAddress(),
                isAudit() ? getComponent().getServerAuditStrategy() : null,
                getAuditContext(),
                getCustomInterceptors(),
                getRejectionHandlingStrategy());
    }

    @Override
    public AbstractWsProducer<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>, ?, ?> getProducer(
            AbstractWsEndpoint<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>> endpoint,
            JaxWsClientFactory<ChPpqAuditDataset> clientFactory)
    {
        return new ChPpqProducer(endpoint, clientFactory);
    }

}
