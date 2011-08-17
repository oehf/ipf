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
package org.openehealth.ipf.platform.camel.ihe.xds.iti14;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xds.core.XdsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.core.XdsServiceFactory;
import org.openehealth.ipf.commons.ihe.xds.iti14.Iti14ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti14.Iti14ServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * The Camel endpoint for the ITI-14 transaction.
 */
public class Iti14Endpoint extends DefaultItiEndpoint<ItiServiceInfo> {

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti14Component
     *          the component creating this endpoint.
     */
    public Iti14Endpoint(
            String endpointUri, 
            String address, 
            Iti14Component iti14Component,
            InterceptorProvider customInterceptors) 
    {
        super(endpointUri, address, iti14Component, customInterceptors);
    }

    @Override
    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new XdsClientFactory(
                getWebServiceConfiguration(),
                getServiceUrl(),
                isAudit() ? new Iti14ClientAuditStrategy(isAllowIncompleteAudit()) : null,
                null,
                getCustomInterceptors());
        return new Iti14Producer(this, clientFactory);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new XdsServiceFactory(
                getWebServiceConfiguration(),
                isAudit() ? new Iti14ServerAuditStrategy(isAllowIncompleteAudit()) : null, 
                getServiceAddress(),
                getCustomInterceptors(),
                getRejectionHandlingStrategy());
        ServerFactoryBean serverFactory =
            serviceFactory.createServerFactory(Iti14Service.class);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
