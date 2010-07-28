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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti46;

import javax.xml.namespace.QName;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ClientFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceFactory;
import org.openehealth.ipf.commons.ihe.pixpdqv3.iti46.Iti46PortType;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * The Camel endpoint for the ITI-46 transaction.
 */
public class Iti46Endpoint extends DefaultItiEndpoint {
    private static final String NS_URI = "urn:ihe:iti:pixv3:2007"; 
    private final static ItiServiceInfo ITI_46 = new ItiServiceInfo(
            new QName(NS_URI, "PIXConsumer_Service", "ihe"),
            Iti46PortType.class,
            new QName(NS_URI, "PIXConsumer_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti46/iti46-raw.wsdl",
            false,
            false);

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti46Component
     *          the component creating this endpoint.
     */
    public Iti46Endpoint(
            String endpointUri, 
            String address, 
            Iti46Component iti46Component) {
        super(endpointUri, address, iti46Component);
    }

    @Override
    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = new Hl7v3ClientFactory(ITI_46, getServiceUrl());
        return new Iti46Producer(this, clientFactory);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = new Hl7v3ServiceFactory(ITI_46, getServiceAddress());
        ServerFactoryBean serverFactory =
            serviceFactory.createServerFactory(Iti46Service.class);
        Server server = serverFactory.create();
        DefaultItiWebService service = (DefaultItiWebService) serverFactory.getServiceBean();
        return new DefaultItiConsumer(this, processor, service, server);
    }
}
