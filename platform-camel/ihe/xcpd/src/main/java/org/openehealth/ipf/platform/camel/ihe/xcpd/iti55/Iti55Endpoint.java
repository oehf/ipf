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
package org.openehealth.ipf.platform.camel.ihe.xcpd.iti55;

import java.net.URISyntaxException;

import javax.xml.namespace.QName;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xcpd.XcpdClientFactory;
import org.openehealth.ipf.commons.ihe.xcpd.XcpdServiceFactory;
import org.openehealth.ipf.commons.ihe.xcpd.iti55.Iti55ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xcpd.iti55.Iti55PortType;
import org.openehealth.ipf.commons.ihe.xcpd.iti55.Iti55ServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * The Camel endpoint for the ITI-55 transaction.
 */
public class Iti55Endpoint extends DefaultItiEndpoint {
    private final static String NS_URI = "urn:ihe:iti:xcpd:2009";
    private final static ItiServiceInfo ITI_55 = new ItiServiceInfo(
            new QName(NS_URI, "RespondingGateway_Service", "xcpd"),
            Iti55PortType.class,
            new QName(NS_URI, "RespondingGateway_Binding_Soap12", "xcpd"),
            false,
            "wsdl/iti55/iti55-raw.wsdl",
            true,
            false);

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti55Component
     *          the component creating this endpoint.
     * @throws URISyntaxException
     *          if the endpoint URI was not a valid URI.
     */
    public Iti55Endpoint(
            String endpointUri, 
            String address, 
            Iti55Component iti55Component) throws URISyntaxException 
    {
        super(endpointUri, address, iti55Component);
    }

    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new XcpdClientFactory(
                ITI_55,
                isAudit() ? new Iti55ClientAuditStrategy(isAllowIncompleteAudit()) : null,
                getServiceUrl(),
                getCorrelator());
        return new Iti55Producer(this, clientFactory);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new XcpdServiceFactory(
                ITI_55,
                isAudit() ? new Iti55ServerAuditStrategy(isAllowIncompleteAudit()) : null,
                getServiceAddress());
        ServerFactoryBean serverFactory =
            serviceFactory.createServerFactory(Iti55Service.class);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
