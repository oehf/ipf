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
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsServiceFactory;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.Hl7v2InterceptorUtils;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerAdaptingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerRequestAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerResponseAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultWsConsumer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Camel endpoint for HL7v2-WS transaction with a single operation.
 */
public class SimpleHl7v2WsEndpoint extends AbstractWsEndpoint<AbstractHl7v2WsComponent> implements HL7v2Endpoint {

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param component
     *          the component creating this endpoint.
     * @param customInterceptors
     *          user-defined CXF interceptors.
     * @param features
     *          user-defined list of CXF features.
     */
    public SimpleHl7v2WsEndpoint(
            String endpointUri,
            String address,
            AbstractHl7v2WsComponent component,
            InterceptorProvider customInterceptors,
            List<AbstractFeature> features,
            List<String> schemaLocations,
            Map<String, Object> properties)
    {
        super(endpointUri, address, component, customInterceptors, features, schemaLocations, properties);
    }


    protected List<Interceptor> getProducerInterceptorChain() {
        return Arrays.<Interceptor> asList(
                new ProducerMarshalInterceptor(),
                new ProducerResponseAcceptanceInterceptor(),
                new ProducerRequestAcceptanceInterceptor(),
                new ProducerAdaptingInterceptor()
        );
    }


    @Override
    public Producer createProducer() throws Exception {
        return Hl7v2InterceptorUtils.adaptProducerChain(
                getProducerInterceptorChain(),
                this,
                getComponent().getProducer(this, getJaxWsClientFactory()));
    }


    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        AbstractHl7v2WebService serviceInstance =
                (AbstractHl7v2WebService) getComponent().getServiceInstance(this);
        serviceInstance.setHl7v2Configuration(getComponent());
        ServerFactoryBean serverFactory = getJaxWsServiceFactory().createServerFactory(serviceInstance);
        Server server = serverFactory.create();
        AbstractWebService service = (AbstractWebService) serverFactory.getServiceBean();
        return new DefaultWsConsumer(this, processor, service, server);
    }


    @Override
    public JaxWsClientFactory getJaxWsClientFactory() {
        return new JaxWsClientFactory(
                getComponent().getWsTransactionConfiguration(),
                getServiceUrl(),
                null,
                getCustomInterceptors());
    }


    @Override
    public JaxWsServiceFactory getJaxWsServiceFactory() {
        return new JaxWsServiceFactory(
                getComponent().getWsTransactionConfiguration(),
                getServiceAddress(),
                null,
                getCustomInterceptors(),
                getRejectionHandlingStrategy());
    }

    @Override
    public Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration() {
        return getComponent().getHl7v2TransactionConfiguration();
    }

    @Override
    public NakFactory getNakFactory() {
        return getComponent().getNakFactory();
    }
}
