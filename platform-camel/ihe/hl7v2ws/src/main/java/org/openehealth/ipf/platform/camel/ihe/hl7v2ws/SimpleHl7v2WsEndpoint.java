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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2InteractionId;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.*;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorUtils;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerAdaptingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerRequestAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerResponseAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultWsConsumer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Camel endpoint for HL7v2-WS transaction with a single operation.
 */
public abstract class SimpleHl7v2WsEndpoint<
        ComponentType extends AbstractHl7v2WsComponent>
        extends AbstractWsEndpoint<WsAuditDataset, WsTransactionConfiguration<WsAuditDataset>> implements HL7v2Endpoint<MllpAuditDataset> {

    public SimpleHl7v2WsEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<WsAuditDataset, WsTransactionConfiguration<WsAuditDataset>, ? extends WsInteractionId<WsTransactionConfiguration<WsAuditDataset>>> component,
            Map<String, Object> parameters,
            Class<? extends AbstractWebService> serviceClass) {
        super(endpointUri, address, component, parameters, serviceClass);
    }

    protected List<Interceptor> getProducerInterceptorChain() {
        return Arrays.asList(
                new ProducerMarshalInterceptor(),
                new ProducerResponseAcceptanceInterceptor(),
                new ProducerRequestAcceptanceInterceptor(),
                new ProducerAdaptingInterceptor()
        );
    }


    @Override
    public Producer createProducer() {
        return InterceptorUtils.adaptProducerChain(
                getProducerInterceptorChain(),
                this,
                getProducer(this, getJaxWsClientFactory()));
    }


    @Override
    public Consumer createConsumer(Processor processor) {
        AbstractHl7v2WebService serviceInstance = (AbstractHl7v2WebService) getServiceInstance();
        serviceInstance.setHl7v2Configuration(this);
        ServerFactoryBean serverFactory = getJaxWsServiceFactory().createServerFactory(serviceInstance);
        Server server = serverFactory.create();
        AbstractWebService service = (AbstractWebService) serverFactory.getServiceBean();
        return new DefaultWsConsumer<>(this, processor, service, server);
    }


    @Override
    public JaxWsClientFactory<WsAuditDataset> getJaxWsClientFactory() {
        return new JaxWsRequestClientFactory<>(
                getComponent().getWsTransactionConfiguration(),
                getServiceUrl(),
                null,
                getAuditContext(),
                getCustomCxfInterceptors(),
                getFeatures(),
                getProperties(),
                null,
                getSecurityInformation());
    }


    @Override
    public JaxWsServiceFactory<WsAuditDataset> getJaxWsServiceFactory() {
        return new JaxWsRequestServiceFactory<>(
                getComponent().getWsTransactionConfiguration(),
                getServiceAddress(),
                null,
                getAuditContext(),
                getCustomCxfInterceptors(),
                getRejectionHandlingStrategy());
    }

    @SuppressWarnings("unchecked")
    @Override
    public ComponentType getComponent() {
        return (ComponentType) super.getComponent();
    }

    @Override
    public Hl7v2TransactionConfiguration<MllpAuditDataset> getHl7v2TransactionConfiguration() {
        return getComponent().getHl7v2TransactionConfiguration();
    }

    @Override
    public NakFactory<MllpAuditDataset> getNakFactory() {
        return getComponent().getNakFactory();
    }

    @Override
    public Hl7v2InteractionId<MllpAuditDataset> getInteractionId() {
        return getComponent().getInteractionId();
    }
}
