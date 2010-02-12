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

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.pixpdqv3.iti44.Iti44XdsPortType;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiProducer;

/**
 * The producer implementation for the ITI-44 component (XDS.b).
 */
public class Iti44XdsProducer extends DefaultItiProducer {
    /**
     * Constructs the producer.
     * @param endpoint
     *          the endpoint creating this producer.
     * @param clientFactory
     *          the factory for clients to produce messages for the service.              
     */
    public Iti44XdsProducer(DefaultItiEndpoint endpoint, ItiClientFactory clientFactory) {
        super(endpoint, clientFactory);
    }

    @Override
    protected void callService(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        Iti44XdsPortType client = (Iti44XdsPortType) getClient();
        String result = client.documentRegistryPRPAIN201301UV02(body);
        Exchanges.resultMessage(exchange).setBody(result);
    }
}
