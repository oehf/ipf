/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds;

import org.openehealth.ipf.commons.ihe.ws.*;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import java.util.Map;

/**
 * Camel Endpoint implementation for XDS-like transactions
 * which have only a single Web Service operation.
 *
 * @author Dmytro Rud
 */
public class XdsAsyncResponseEndpoint<AuditDatasetType extends XdsAuditDataset> extends AbstractWsEndpoint<AuditDatasetType, WsTransactionConfiguration<AuditDatasetType>> {

    public XdsAsyncResponseEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<AuditDatasetType, WsTransactionConfiguration<AuditDatasetType>, ? extends WsInteractionId<WsTransactionConfiguration<AuditDatasetType>>> component,
            Map<String, Object> parameters,
            Class<? extends AbstractWebService> serviceClass) {
        super(endpointUri, address, component, parameters, serviceClass);
    }

    @Override
    public JaxWsClientFactory<AuditDatasetType> getJaxWsClientFactory() {
        return null;   // no producer support
    }


    @Override
    public JaxWsServiceFactory<AuditDatasetType> getJaxWsServiceFactory() {
        return new JaxWsAsyncResponseServiceFactory<>(
                getComponent().getWsTransactionConfiguration(),
                getServiceAddress(),
                isAudit() ? getComponent().getServerAuditStrategy() : null,
                getAuditContext(),
                getCustomInterceptors(),
                getCorrelator());
    }

    @Override
    public AbstractWsProducer<AuditDatasetType, WsTransactionConfiguration<AuditDatasetType>, ?, ?> getProducer(AbstractWsEndpoint<AuditDatasetType, WsTransactionConfiguration<AuditDatasetType>> endpoint,
                                                                                                                JaxWsClientFactory<AuditDatasetType> clientFactory) {
        throw new IllegalStateException("No producer support for asynchronous response endpoints");
    }
}
