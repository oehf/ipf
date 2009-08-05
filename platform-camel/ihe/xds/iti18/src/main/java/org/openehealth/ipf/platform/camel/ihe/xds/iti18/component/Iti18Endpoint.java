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
package org.openehealth.ipf.platform.camel.ihe.xds.iti18.component;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.iti18.service.Iti18PortType;

import javax.xml.namespace.QName;
import java.net.URISyntaxException;

/**
 * The endpoint implementation for the ITI-18 component.
 */
public class Iti18Endpoint extends DefaultItiEndpoint {
    private static final ItiServiceInfo<Iti18PortType> SERVICE_INFO = new ItiServiceInfo<Iti18PortType>(
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti18PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Port_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Port_Soap12", "ihe"),
            false,
            "wsdl/iti18.wsdl",
            true);

    /**
     * Constructs the endpoint
     * @param endpointUri
     *          the URI of the endpoint.
     * @param address
     *          the endpoint address from the URI.
     * @param iti18Component
     *          the component creating this endpoint.
     * @throws URISyntaxException
     *          if the endpoint URI was not a valid URI.
     */
    public Iti18Endpoint(String endpointUri, String address, Iti18Component iti18Component) throws URISyntaxException {
        super(endpointUri, address, iti18Component);
    }

    public Producer createProducer() throws Exception {
        return new Iti18Producer(this, SERVICE_INFO);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new Iti18Consumer(this, processor, SERVICE_INFO);
    }
}
