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
package org.openehealth.ipf.platform.camel.ihe.xca.iti38;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xca.XcaClientFactory;
import org.openehealth.ipf.commons.ihe.xca.XcaServiceFactory;
import org.openehealth.ipf.commons.ihe.xca.iti38.Iti38ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xca.iti38.Iti38ServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * The endpoint implementation for the ITI-38 component.
 */
public class Iti38Endpoint extends DefaultItiEndpoint<ItiServiceInfo> {

    public Iti38Endpoint(
            String endpointUri,
            String address,
            Iti38Component iti38Component,
            InterceptorProvider customInterceptors)
    {
        super(endpointUri, address, iti38Component, customInterceptors);
    }


    @Override
    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new XcaClientFactory(
                getWebServiceConfiguration(),
                isAudit() ? new Iti38ClientAuditStrategy(isAllowIncompleteAudit()) : null,
                getServiceUrl(),
                getCorrelator(),
                getCustomInterceptors());
        return new Iti38Producer(this, clientFactory);
    }


    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new XcaServiceFactory(
                getWebServiceConfiguration(),
                isAudit() ? new Iti38ServerAuditStrategy(isAllowIncompleteAudit()) : null,
                getServiceAddress(),
                getCustomInterceptors(),
                getRejectionHandlingStrategy());
        ServerFactoryBean serverFactory =
            serviceFactory.createServerFactory(new Iti38Service(this));
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
