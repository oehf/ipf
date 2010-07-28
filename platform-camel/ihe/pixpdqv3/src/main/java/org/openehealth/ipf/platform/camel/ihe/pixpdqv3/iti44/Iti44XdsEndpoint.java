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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti44;

import java.net.URISyntaxException;

import javax.xml.namespace.QName;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ClientFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceFactory;
import org.openehealth.ipf.commons.ihe.pixpdqv3.iti44.Iti44XdsPortType;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * The Camel endpoint for the ITI-44 transaction (XDS.b).
 */
public class Iti44XdsEndpoint extends DefaultItiEndpoint {
    private static final String NS_URI = "urn:ihe:iti:xds-b:2007";
    private final static ItiServiceInfo ITI_44 = new ItiServiceInfo(
            new QName(NS_URI, "DocumentRegistry_Service", "ihe"),
            Iti44XdsPortType.class,
            new QName(NS_URI, "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti44/iti44-xds-raw.wsdl",
            false,
            false);

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti44Component
     *          the component creating this endpoint.
     * @throws URISyntaxException
     *          if the endpoint URI was not a valid URI.
     */
    public Iti44XdsEndpoint(
            String endpointUri, 
            String address, 
            Iti44XdsComponent iti44Component) {
        super(endpointUri, address, iti44Component);
    }

    @Override
    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new Hl7v3ClientFactory(ITI_44, getServiceUrl());
        return new Iti44XdsProducer(this, clientFactory);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new Hl7v3ServiceFactory(ITI_44, getServiceAddress());
        ServerFactoryBean serverFactory =
            serviceFactory.createServerFactory(Iti44Service.class);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
