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

package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq2;

import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xacml20.audit.ChPpqAuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;
import org.openehealth.ipf.platform.camel.ihe.ws.*;
import org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20Endpoint;

import java.util.Map;

public class ChPpq2Endpoint extends Xacml20Endpoint<ChPpqAuditDataset> {

    public ChPpq2Endpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>, ? extends WsInteractionId<WsTransactionConfiguration<ChPpqAuditDataset>>> component,
            Map<String, Object> parameters)
    {
        super(endpointUri, address, component, parameters, ChPpq2Service.class);
    }

    @Override
    protected AbstractWebService getCustomServiceInstance(AbstractWsEndpoint<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>> endpoint) {
        return new ChPpq2Service(endpoint.getHomeCommunityId());
    }

    @Override
    public AbstractWsProducer<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>, ?, ?> getProducer(AbstractWsEndpoint<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>> endpoint, JaxWsClientFactory<ChPpqAuditDataset> clientFactory) {
        return new SimpleWsProducer<>(this, clientFactory, XACMLPolicyQueryType.class, ResponseType.class);
    }

}
