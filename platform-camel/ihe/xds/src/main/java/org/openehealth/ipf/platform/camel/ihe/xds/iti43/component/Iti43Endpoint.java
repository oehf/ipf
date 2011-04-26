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
package org.openehealth.ipf.platform.camel.ihe.xds.iti43.component;

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
import org.openehealth.ipf.commons.ihe.xds.iti43.Iti43ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti43.Iti43ServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;
import org.openehealth.ipf.platform.camel.ihe.xds.iti43.service.Iti43Service;

/**
 * The Camel endpoint for the ITI-43 transaction.
 */
public class Iti43Endpoint extends DefaultItiEndpoint<ItiServiceInfo> {

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti43Component
     *          the component creating this endpoint.
     */
    public Iti43Endpoint(
            String endpointUri, 
            String address, 
            Iti43Component iti43Component,
            InterceptorProvider customInterceptors)  
    {
        super(endpointUri, address, iti43Component, customInterceptors);
    }

    @Override public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new XdsClientFactory(
                getWebServiceConfiguration(),
                isAudit() ? new Iti43ClientAuditStrategy(isAllowIncompleteAudit()) : null, 
                getServiceUrl(),
                getCustomInterceptors());
        return new Iti43Producer(this, clientFactory);
    }

    @Override public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new XdsServiceFactory(
                getWebServiceConfiguration(),
                isAudit() ? new Iti43ServerAuditStrategy(isAllowIncompleteAudit()) : null,
                getServiceAddress(),
                getCustomInterceptors());
        ServerFactoryBean serverFactory =
            serviceFactory.createServerFactory(Iti43Service.class);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}