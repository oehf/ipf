/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xca.iti38.asyncresponse;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xca.XcaAsyncResponseServiceFactory;
import org.openehealth.ipf.commons.ihe.xca.iti38.Iti38ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xca.iti38.asyncresponse.Iti38AsyncResponsePortType;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

import javax.xml.namespace.QName;
import java.net.URISyntaxException;

/**
 * The Camel endpoint for the ITI-38 async response.
 */
public class Iti38AsyncResponseEndpoint extends DefaultItiEndpoint {
    private final static ItiServiceInfo ITI_38_ASYNC_RESPONSE = new ItiServiceInfo(
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Response_Service", "ihe"),
            Iti38AsyncResponsePortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Response_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti38-asyncresponse.wsdl",
            true,
            false,
            false);

    /**
     * Constructs the endpoint.
     */
    public Iti38AsyncResponseEndpoint(
            String endpointUri,
            String address,
            Iti38AsyncResponseComponent iti38AsyncResponseComponent,
            InterceptorProvider customInterceptors) throws URISyntaxException 
    {
        super(endpointUri, address, iti38AsyncResponseComponent, customInterceptors);
    }

    public Producer createProducer() throws Exception {
        throw new IllegalStateException("No producer support for asynchronous response endpoints");
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new XcaAsyncResponseServiceFactory(
                ITI_38_ASYNC_RESPONSE,
                isAudit() ? new Iti38ClientAuditStrategy(isAllowIncompleteAudit()) : null,
                getServiceAddress(),
                getCorrelator(),
                getCustomInterceptors());
        ServerFactoryBean serverFactory =
            serviceFactory.createServerFactory(Iti38AsyncResponseService.class);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
