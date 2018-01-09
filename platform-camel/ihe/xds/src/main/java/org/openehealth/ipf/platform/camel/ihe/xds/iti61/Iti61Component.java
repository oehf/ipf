/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti61;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsComponent;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsEndpoint;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_61;

/**
 * The Camel component for the ITI-61 transaction.
 */
public class Iti61Component extends XdsComponent<XdsSubmitAuditDataset> {


    public Iti61Component() {
        super(ITI_61);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new XdsEndpoint<XdsSubmitAuditDataset>(uri, remaining, this, parameters, Iti61Service.class) {
            @Override
            public AbstractWsProducer<XdsSubmitAuditDataset, WsTransactionConfiguration<XdsSubmitAuditDataset>, ?, ?> getProducer(AbstractWsEndpoint<XdsSubmitAuditDataset, WsTransactionConfiguration<XdsSubmitAuditDataset>> endpoint,
                                                                                                           JaxWsClientFactory<XdsSubmitAuditDataset> clientFactory) {
                return new SimpleWsProducer<>(
                        endpoint, clientFactory, SubmitObjectsRequest.class, RegistryResponseType.class);
            }
        };
    }


}
