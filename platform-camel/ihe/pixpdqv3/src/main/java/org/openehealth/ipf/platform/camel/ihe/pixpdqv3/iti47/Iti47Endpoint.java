/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti47;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ClientFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ContinuationAwareServiceInfo;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceFactory;
import org.openehealth.ipf.commons.ihe.pixpdqv3.iti47.Iti47PortType;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.Hl7v3ContinuationAwareProducer;
import org.openehealth.ipf.platform.camel.ihe.pixpdqv3.Hl7v3Endpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * The Camel endpoint for the ITI-47 transaction.
 */
public class Iti47Endpoint extends Hl7v3Endpoint<Hl7v3ContinuationAwareServiceInfo> {

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti47Component
     *          the component creating this endpoint.
     */
    public Iti47Endpoint(
            String endpointUri, 
            String address, 
            Iti47Component iti47Component,
            InterceptorProvider customInterceptors) 
    {
        super(endpointUri, address, iti47Component, customInterceptors);
    }

    @Override
    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new Hl7v3ClientFactory(
                getWebServiceConfiguration(),
                getServiceUrl(),
                getCustomInterceptors());
        return new Hl7v3ContinuationAwareProducer(
                this,
                clientFactory,
                getWebServiceConfiguration(),
                isSupportContinuation(),
                isAutoCancel(),
                isValidationOnContinuation());
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new Hl7v3ServiceFactory(
                getWebServiceConfiguration(),
                getServiceAddress(),
                getCustomInterceptors());

        Iti47PortType portTypeImpl = isSupportContinuation() ?
                new Iti47ContinuationAwareService(
                        getContinuationStorage(),
                        getDefaultContinuationThreshold(),
                        isValidationOnContinuation()) :
                new Iti47Service();

        ServerFactoryBean serverFactory = serviceFactory.createServerFactory(portTypeImpl);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
