/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.ech.ech0214;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ech.ECH;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2.Request;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2.Response;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ech.EchEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;

import java.util.Map;

public class Ech0214Component extends AbstractWsComponent<WsAuditDataset, WsTransactionConfiguration<WsAuditDataset>, WsInteractionId<WsTransactionConfiguration<WsAuditDataset>>> {

    public Ech0214Component() {
        super(ECH.Interactions.ECH_0214);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return new EchEndpoint(uri, remaining, this, parameters, Ech0214Service.class) {
            @Override
            public AbstractWsProducer<WsAuditDataset, WsTransactionConfiguration<WsAuditDataset>, ?, ?> getProducer(AbstractWsEndpoint<WsAuditDataset, WsTransactionConfiguration<WsAuditDataset>> endpoint, JaxWsClientFactory<WsAuditDataset> clientFactory) {
                return new SimpleWsProducer<>(endpoint, clientFactory, Request.class, Response.class);
            }
        };

    }
}
