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

import java.net.URISyntaxException;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.commons.lang.Validate;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;

/**
 * The Camel endpoint for the ITI-17 transaction.
 */
public class Iti17Endpoint extends DefaultItiEndpoint {
    private Iti17Consumer activeConsumer;

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

    public Producer createProducer() throws Exception {
        return new Iti17Producer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new Iti17Consumer(this, processor);
    }

    public Iti17Consumer getActiveConsumer() {
        return activeConsumer;
    }

    public void setActiveConsumer(Iti17Consumer activeConsumer) {
        Validate.isTrue(this.activeConsumer == null || activeConsumer == null,
                "Only one ITI-17 consumer can be active at any time");
        this.activeConsumer = activeConsumer;
    }
}
