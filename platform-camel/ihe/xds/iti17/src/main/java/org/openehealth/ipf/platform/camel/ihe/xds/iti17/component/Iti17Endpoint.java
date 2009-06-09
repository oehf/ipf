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
package org.openehealth.ipf.platform.camel.ihe.xds.iti17.component;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiEndpoint;

import java.net.URISyntaxException;

/**
 * The Camel endpoint for the ITI-17 transaction.
 */
public class Iti17Endpoint extends DefaultItiEndpoint {
    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address 
     *          the endpoint address from the URI.
     * @param iti17Component
     *          the component creating this endpoint.
     * @throws URISyntaxException
     *          if the endpoint URI was not a valid URI.
     */
    public Iti17Endpoint(String endpointUri, String address, Iti17Component iti17Component) throws URISyntaxException {
        super(endpointUri, address, iti17Component);
    }

    public Producer<Exchange> createProducer() throws Exception {
        return new Iti17Producer(this);
    }

    public Consumer<Exchange> createConsumer(Processor processor) throws Exception {
        return new Iti17Consumer(this, processor);
    }
}
