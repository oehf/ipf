/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hpd;

import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse;
import org.openehealth.ipf.commons.ihe.ws.*;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ws.*;

import java.util.List;
import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class HpdEndpoint<AuditDatasetType extends WsAuditDataset> extends AbstractWsEndpoint<AuditDatasetType, WsTransactionConfiguration> {

    public HpdEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<AuditDatasetType, WsTransactionConfiguration, ? extends WsInteractionId> component,
            InterceptorProvider customInterceptors,
            List<AbstractFeature> features,
            List<String> schemaLocations,
            Map<String, Object> properties,
            Class<? extends AbstractWebService> serviceClass)
    {
        super(endpointUri, address, component, customInterceptors, features, schemaLocations, properties, serviceClass);
    }

    @Override
    public JaxWsClientFactory<AuditDatasetType> getJaxWsClientFactory() {
        return new JaxWsRequestClientFactory<>(
                getComponent().getWsTransactionConfiguration(),
                getServiceUrl(),
                isAudit() ? getClientAuditStrategy() : null,
                getCustomInterceptors(),
                getFeatures(),
                getProperties(),
                getCorrelator());
    }

    @Override
    public JaxWsServiceFactory<AuditDatasetType> getJaxWsServiceFactory() {
        return new JaxWsRequestServiceFactory<>(
                getComponent().getWsTransactionConfiguration(),
                getServiceAddress(),
                isAudit() ? getComponent().getServerAuditStrategy() : null,
                getCustomInterceptors(),
                getRejectionHandlingStrategy());
    }

    @Override
    public AbstractWsProducer<AuditDatasetType, WsTransactionConfiguration, ?, ?> getProducer(
            AbstractWsEndpoint<AuditDatasetType, WsTransactionConfiguration> endpoint,
            JaxWsClientFactory<AuditDatasetType> clientFactory)
    {
        return new SimpleWsProducer<>(endpoint, clientFactory, BatchRequest.class, BatchResponse.class);
    }

}
