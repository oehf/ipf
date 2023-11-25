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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chadr;

import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xacml20.chadr.ChAdrAuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLAuthzDecisionQueryType;
import org.openehealth.ipf.platform.camel.ihe.ws.*;
import org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20Endpoint;

import java.util.Map;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
public class ChAdrEndpoint extends Xacml20Endpoint<ChAdrAuditDataset> {

    public ChAdrEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<ChAdrAuditDataset, WsTransactionConfiguration<ChAdrAuditDataset>, ? extends WsInteractionId<WsTransactionConfiguration<ChAdrAuditDataset>>> component,
            Map<String, Object> parameters)
    {
        super(endpointUri, address, component, parameters, ChAdrService.class);
    }

    @Override
    protected AbstractWebService getCustomServiceInstance(AbstractWsEndpoint<ChAdrAuditDataset, WsTransactionConfiguration<ChAdrAuditDataset>> endpoint) {
        return new ChAdrService(endpoint.getHomeCommunityId());
    }

    @Override
    public AbstractWsProducer<ChAdrAuditDataset, WsTransactionConfiguration<ChAdrAuditDataset>, ?, ?> getProducer(AbstractWsEndpoint<ChAdrAuditDataset, WsTransactionConfiguration<ChAdrAuditDataset>> endpoint, JaxWsClientFactory<ChAdrAuditDataset> clientFactory) {
        return new SimpleWsProducer<>(this, clientFactory, XACMLAuthzDecisionQueryType.class, ResponseType.class);
    }

}
