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
import org.apache.commons.lang3.Validate;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerAdaptingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerInputAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerOutputAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * Camel endpoint for HL7v2-WS transaction with a single operation.
 */
public class SimpleHl7v2WsEndpoint extends DefaultItiEndpoint<ItiServiceInfo> {

    private final Class<? extends AbstractHl7v2WebService> serviceClass;

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
     * @param serviceClass
     *          service class.
     */
    public SimpleHl7v2WsEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<ItiServiceInfo> component,
            InterceptorProvider customInterceptors,
            Class<? extends AbstractHl7v2WebService> serviceClass)
    {
        super(endpointUri, address, component, customInterceptors);
        Validate.notNull(serviceClass);
        this.serviceClass = serviceClass;
    }


    protected static Producer wrapProducer(
            Hl7v2ConfigurationHolder configurationHolder,
            Producer producer)
    {
        producer = new ProducerMarshalInterceptor(configurationHolder, producer);
        producer = new ProducerOutputAcceptanceInterceptor(configurationHolder, producer);
        producer = new ProducerInputAcceptanceInterceptor(configurationHolder, producer);
        producer = new ProducerAdaptingInterceptor(configurationHolder, producer);
        return producer;
    }


    @Override
    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new ItiClientFactory(
                getWebServiceConfiguration(),
                getServiceUrl(),
                null,
                getCustomInterceptors());
        return wrapProducer((Hl7v2ConfigurationHolder) getComponent(),
                new SimpleHl7v2WsProducer(this, clientFactory));
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new ItiServiceFactory(
                getWebServiceConfiguration(),
                getServiceAddress(),
                null,
                getCustomInterceptors(),
                getRejectionHandlingStrategy());

        AbstractHl7v2WebService serviceInstance = serviceClass.newInstance();
        serviceInstance.setHl7v2Configuration((Hl7v2ConfigurationHolder) getComponent());
        ServerFactoryBean serverFactory = serviceFactory.createServerFactory(serviceInstance);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }

}
