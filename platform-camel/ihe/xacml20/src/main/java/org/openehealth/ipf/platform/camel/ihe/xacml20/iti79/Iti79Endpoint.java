/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xacml20.iti79;

import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xacml20.iti79.Iti79AuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLAuthzDecisionQueryType;
import org.openehealth.ipf.platform.camel.ihe.ws.*;
import org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20Endpoint;

import java.util.Map;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
public class Iti79Endpoint extends Xacml20Endpoint<Iti79AuditDataset> {

    public Iti79Endpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<Iti79AuditDataset, WsTransactionConfiguration<Iti79AuditDataset>, ? extends WsInteractionId<WsTransactionConfiguration<Iti79AuditDataset>>> component,
            Map<String, Object> parameters)
    {
        super(endpointUri, address, component, parameters, Iti79Service.class);
    }

    @Override
    protected AbstractWebService getCustomServiceInstance(AbstractWsEndpoint<Iti79AuditDataset, WsTransactionConfiguration<Iti79AuditDataset>> endpoint) {
        return new Iti79Service(endpoint.getHomeCommunityId());
    }

    @Override
    public AbstractWsProducer<Iti79AuditDataset, WsTransactionConfiguration<Iti79AuditDataset>, ?, ?> getProducer(AbstractWsEndpoint<Iti79AuditDataset, WsTransactionConfiguration<Iti79AuditDataset>> endpoint, JaxWsClientFactory<Iti79AuditDataset> clientFactory) {
        return new SimpleWsProducer<>(this, clientFactory, XACMLAuthzDecisionQueryType.class, ResponseType.class);
    }

}
