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
package org.openehealth.ipf.platform.camel.ihe.xds.iti42.component;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.xds.Iti42;
import org.openehealth.ipf.platform.camel.ihe.xds.iti42.service.Iti42Service;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;

/**
 * The Camel endpoint for the ITI-42 transaction.
 */
public class Iti42Endpoint extends DefaultItiEndpoint {
    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti42Component
     *          the component creating this endpoint.
     */
    public Iti42Endpoint(String endpointUri, String address, Iti42Component iti42Component)  {
        super(endpointUri, address, iti42Component);
    }

    @Override
    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = 
            Iti42.getClientFactory(isSoap11(), isAudit(), isAllowIncompleteAudit(), getServiceUrl());
        return new Iti42Producer(this, clientFactory);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = 
            Iti42.getServiceFactory(isAudit(), isAllowIncompleteAudit(), getServiceAddress());
        Iti42Service service = serviceFactory.createService(Iti42Service.class); 
        return new DefaultItiConsumer(this, processor, service);
    }
}