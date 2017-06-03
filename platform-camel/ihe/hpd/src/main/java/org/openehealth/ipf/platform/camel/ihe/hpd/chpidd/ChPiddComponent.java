/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hpd.chpidd;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.hpd.HPD;
import org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadResponse;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.hpd.HpdEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;

import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class ChPiddComponent extends AbstractWsComponent<WsAuditDataset, WsTransactionConfiguration, WsInteractionId<WsTransactionConfiguration>> {

    public ChPiddComponent() {
        super(HPD.Interactions.CH_PIDD);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return new HpdEndpoint<WsAuditDataset>(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                getSslContextParameters(parameters),
                ChPiddService.class)
        {
            @Override
            public AbstractWsProducer<WsAuditDataset, WsTransactionConfiguration, ?, ?> getProducer(AbstractWsEndpoint<WsAuditDataset, WsTransactionConfiguration> endpoint, JaxWsClientFactory<WsAuditDataset> clientFactory) {
                return new SimpleWsProducer<>(endpoint, clientFactory, DownloadRequest.class, DownloadResponse.class);
            }
        };
    }

}
