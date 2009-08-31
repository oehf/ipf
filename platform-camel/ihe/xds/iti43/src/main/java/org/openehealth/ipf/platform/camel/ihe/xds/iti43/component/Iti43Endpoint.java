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
import org.openehealth.ipf.commons.ihe.xds.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.xds.iti43.service.Iti43PortType;

import javax.xml.namespace.QName;
import java.net.URISyntaxException;

/**
 * The Camel endpoint for the ITI-43 transaction.
 */
public class Iti43Endpoint extends DefaultItiEndpoint {
    private static final ItiServiceInfo<Iti43PortType> SERVICE_INFO = new ItiServiceInfo<Iti43PortType>(
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service", "ihe"),
            Iti43PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Binding_Soap12", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Port_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Port_Soap12", "ihe"),
            true,
            "wsdl/iti43.wsdl",
            true);

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti43Component
     *          the component creating this endpoint.
     * @throws URISyntaxException
     *          if the endpoint URI was not a valid URI.
     */
    public Iti43Endpoint(String endpointUri, String address, Iti43Component iti43Component) throws URISyntaxException {
        super(endpointUri, address, iti43Component);
    }

    public Producer createProducer() throws Exception {
        return new Iti43Producer(this, SERVICE_INFO);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new Iti43Consumer(this, processor, SERVICE_INFO);
    }
}