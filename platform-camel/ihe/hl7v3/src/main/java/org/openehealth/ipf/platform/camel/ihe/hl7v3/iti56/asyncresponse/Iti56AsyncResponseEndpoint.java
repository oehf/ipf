/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti56.asyncresponse;

import java.net.URISyntaxException;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceInfo;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AsyncResponseServiceFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.iti56.Iti56ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * The Camel endpoint for the ITI-56 async response.
 */
public class Iti56AsyncResponseEndpoint extends DefaultItiEndpoint<Hl7v3ServiceInfo> {

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti56AsyncResponseComponent
     *          the component creating this endpoint.
     * @param customInterceptors
     *          user-defined additional CXF interceptors.
     * @throws URISyntaxException
     *          if the endpoint URI was not a valid URI.
     */
    public Iti56AsyncResponseEndpoint(
            String endpointUri, 
            String address, 
            Iti56AsyncResponseComponent iti56AsyncResponseComponent,
            InterceptorProvider customInterceptors) throws URISyntaxException 
    {
        super(endpointUri, address, iti56AsyncResponseComponent, customInterceptors);
    }

    public Producer createProducer() throws Exception {
        throw new IllegalStateException("No producer support for asynchronous response endpoints");
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new Hl7v3AsyncResponseServiceFactory(
                getWebServiceConfiguration(),
                getServiceAddress(),
                isAudit() ? new Iti56ClientAuditStrategy(isAllowIncompleteAudit()) : null,
                getCorrelator(),
                getCustomInterceptors());
        ServerFactoryBean serverFactory = 
            serviceFactory.createServerFactory(Iti56AsyncResponseService.class);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
