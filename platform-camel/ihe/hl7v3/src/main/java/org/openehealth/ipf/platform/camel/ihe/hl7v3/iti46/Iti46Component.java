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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti46;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Component;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Endpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.hl7v3.PIXV3.Interactions.ITI_46;

/**
 * The Camel component for the ITI-46 transaction (PIX v3).
 */
public class Iti46Component extends Hl7v3Component<Hl7v3WsTransactionConfiguration> {

    public Iti46Component() {
        super(ITI_46);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new Hl7v3Endpoint<Hl7v3WsTransactionConfiguration>(uri, remaining, this,
                getAuditContext(parameters),
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                Iti46Service.class) {

            @Override
            public AbstractWsProducer<Hl7v3AuditDataset, Hl7v3WsTransactionConfiguration, ?, ?> getProducer(AbstractWsEndpoint<Hl7v3AuditDataset, Hl7v3WsTransactionConfiguration> endpoint, JaxWsClientFactory<Hl7v3AuditDataset> clientFactory) {
                return new SimpleWsProducer<>(endpoint, clientFactory, String.class, String.class);
            }

        };
    }


}
