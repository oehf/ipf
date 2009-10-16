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
import org.openehealth.ipf.commons.ihe.xds.Iti18;
import org.openehealth.ipf.commons.ihe.xds.core.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ItiServiceFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.core.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.xds.core.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.xds.iti18.service.Iti18Service;

/**
 * The endpoint implementation for the ITI-18 component.
 */
public class Iti18Endpoint extends DefaultItiEndpoint {
    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the URI of the endpoint.
     * @param address
     *          the endpoint address from the URI.
     * @param iti18Component
     *          the component creating this endpoint.
     */
    public Iti18Endpoint(String endpointUri, String address, Iti18Component iti18Component)  {
        super(endpointUri, address, iti18Component);
    }

    @Override
    public Producer createProducer() throws Exception {
        ItiClientFactory clientFactory = Iti18.getClientFactory(isSoap11(), isAudit(), isAllowIncompleteAudit(), getServiceUrl());
        return new Iti18Producer(this, clientFactory);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ItiServiceFactory serviceFactory = 
            Iti18.getServiceFactory(isAudit(), isAllowIncompleteAudit(), getServiceAddress());
        Iti18Service service = serviceFactory.createService(Iti18Service.class);
        return new DefaultItiConsumer(this, processor, service);
    }
}
