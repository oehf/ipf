/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AsyncResponseServiceFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.audit.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3DeferredResponderFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import java.util.Map;

/**
 * Camel endpoint implementation for asynchronous response
 * receivers of HL7v3-based IHE components.
 * @author Dmytro Rud
 */
public class Hl7v3AsyncResponseEndpoint<ConfigType extends Hl7v3WsTransactionConfiguration>
        extends AbstractWsEndpoint<Hl7v3AuditDataset, ConfigType> {

    public Hl7v3AsyncResponseEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<Hl7v3AuditDataset, ConfigType, ? extends WsInteractionId<ConfigType>> component,
            Map<String, Object> parameters,
            Class<? extends AbstractWebService> serviceClass) {
        super(endpointUri, address, component, parameters, serviceClass);
    }

    // currently is used for deferred response receivers only!
    @Override
    public JaxWsClientFactory<Hl7v3AuditDataset> getJaxWsClientFactory() {
        return new Hl7v3DeferredResponderFactory(
                getComponent().getWsTransactionConfiguration(),
                getServiceUrl(),
                isAudit() ? getComponent().getServerAuditStrategy() : null,
                getAuditContext(),
                getCustomCxfInterceptors(),
                getFeatures(),
                getProperties(),
                getSecurityInformation());
    }


    @Override
    public JaxWsServiceFactory<Hl7v3AuditDataset> getJaxWsServiceFactory() {
        return new Hl7v3AsyncResponseServiceFactory(
                getComponent().getWsTransactionConfiguration(),
                getServiceAddress(),
                isAudit() ? getComponent().getServerAuditStrategy() : null,
                getAuditContext(),
                getCorrelator(),
                getCustomCxfInterceptors());
    }

    @Override
    public AbstractWsProducer<Hl7v3AuditDataset, ConfigType, ?, ?> getProducer(AbstractWsEndpoint<Hl7v3AuditDataset, ConfigType> endpoint,
                                          JaxWsClientFactory<Hl7v3AuditDataset> clientFactory) {
        throw new IllegalStateException("No producer support for asynchronous response endpoints");
    }
}
