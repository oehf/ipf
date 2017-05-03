/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws.pcd01;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.hl7v2ws.AbstractHl7v2WsComponent;
import org.openehealth.ipf.platform.camel.ihe.hl7v2ws.SimpleHl7v2WsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.hl7v2ws.PCD.Interactions.PCD_01;


/**
 * Camel component for the IHE PCD-01 transaction.
 */
public class Pcd01Component extends AbstractHl7v2WsComponent<WsAuditDataset> {

    public Pcd01Component() {
        super(PCD_01);
    }

    @Override
    @SuppressWarnings("raw") // Required because of base class
    protected Endpoint createEndpoint(String uri, String remaining, @SuppressWarnings("rawtypes") Map<String, Object> parameters) throws Exception {
        return new SimpleHl7v2WsEndpoint<WsAuditDataset, Pcd01Component>(
                uri,
                remaining,
                this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                getSslContextParameters(parameters),
                Pcd01Service.class) {
            @Override
            public AbstractWsProducer<WsAuditDataset, WsTransactionConfiguration, ?, ?> getProducer(
                    AbstractWsEndpoint<WsAuditDataset, WsTransactionConfiguration> endpoint,
                    JaxWsClientFactory<WsAuditDataset> clientFactory) {
                return new SimpleWsProducer<>(endpoint, clientFactory, String.class, String.class);
            }
        };
    }


}
