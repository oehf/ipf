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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti44;

import org.openehealth.ipf.commons.ihe.hl7v3.iti44.GenericIti44PortType;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiProducer;

/**
 * Producer implementation for the ITI-44 component (PIX Feed v3).
 */
public class Iti44Producer extends DefaultItiProducer<String, String> {
    /**
     * Constructs the producer.
     * @param endpoint
     *          the endpoint creating this producer.
     * @param clientFactory
     *          the factory for clients to produce messages for the service.              
     */
    public Iti44Producer(DefaultItiEndpoint endpoint, ItiClientFactory clientFactory) {
        super(endpoint, clientFactory);
    }

    @Override
    protected String callService(Object clientObject, String request) {
        GenericIti44PortType client = (GenericIti44PortType) clientObject;
        String rootElementName = SoapUtils.getRootElementLocalName(request);
        if ("PRPA_IN201301UV02".equals(rootElementName)) {
            return client.recordAdded(request);
        }
        else if ("PRPA_IN201302UV02".equals(rootElementName)) {
            return client.recordRevised(request);
        }
        else if ("PRPA_IN201304UV02".equals(rootElementName)) {
            return client.duplicatesResolved(request);
        }
        throw new RuntimeException("Cannot dispatch message with root element " + rootElementName);
    }
}
