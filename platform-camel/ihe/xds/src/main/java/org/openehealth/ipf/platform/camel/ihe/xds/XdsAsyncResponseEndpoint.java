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

import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.JaxWsAsyncResponseServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.XdsInteractionId;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import java.util.List;
import java.util.Map;

/**
 * Camel Endpoint implementation for XDS-like transactions
 * which have only a single Web Service operation.
 * @author Dmytro Rud
 */
public class XdsAsyncResponseEndpoint<AuditDatasetType extends XdsAuditDataset> extends AbstractWsEndpoint<AuditDatasetType, WsTransactionConfiguration> {

    public XdsAsyncResponseEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<AuditDatasetType, WsTransactionConfiguration, ? extends XdsInteractionId> component,
            InterceptorProvider customInterceptors,
            List<AbstractFeature> features,
            List<String> schemaLocations,
            Map<String, Object> properties,
            SSLContextParameters sslContextParameters,
            Class<? extends AbstractWebService> serviceClass)
    {
        super(endpointUri, address, component, customInterceptors, features, schemaLocations, properties, sslContextParameters, serviceClass);
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
                getCustomInterceptors(),
                getCorrelator());
    }

    @Override
    public AbstractWsProducer<AuditDatasetType, WsTransactionConfiguration, ?, ?> getProducer(AbstractWsEndpoint<AuditDatasetType, WsTransactionConfiguration> endpoint,
                                          JaxWsClientFactory<AuditDatasetType> clientFactory) {
        throw new IllegalStateException("No producer support for asynchronous response endpoints");
    }
}
