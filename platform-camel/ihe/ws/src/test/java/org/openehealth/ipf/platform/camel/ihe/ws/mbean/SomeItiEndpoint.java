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
package org.openehealth.ipf.platform.camel.ihe.ws.mbean;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import java.util.List;
import java.util.Map;

public class SomeItiEndpoint extends AbstractWsEndpoint<WsAuditDataset, WsTransactionConfiguration<WsAuditDataset>> {

    @SuppressWarnings("unchecked")
    public SomeItiEndpoint(
            String endpointUri,
            String address,
            SomeItiComponent someItiComponent,
            AuditContext auditContext,
            InterceptorProvider interceptorProvider,
            List<AbstractFeature> features,
            List<String> schemaLocations,
            Map<String, Object> properties,
            HTTPClientPolicy httpClientPolicy) {
        super(endpointUri, address, someItiComponent, auditContext,
                interceptorProvider, features, schemaLocations, properties, httpClientPolicy, null);
    }

    @Override
    public JaxWsClientFactory<WsAuditDataset> getJaxWsClientFactory() {
        return null;   // dummy
    }

    @Override
    public JaxWsServiceFactory<WsAuditDataset> getJaxWsServiceFactory() {
        return null;   // dummy
    }

    @Override
    public Producer createProducer() {
        return null;   // dummy
    }

    @Override
    public AbstractWsProducer<WsAuditDataset, WsTransactionConfiguration<WsAuditDataset>, ?, ?> getProducer(AbstractWsEndpoint<WsAuditDataset, WsTransactionConfiguration<WsAuditDataset>> endpoint, JaxWsClientFactory<WsAuditDataset> clientFactory) {
        return null;
    }

    @Override
    public Consumer createConsumer(Processor processor) {
        return null;   // dummy
    }

}
