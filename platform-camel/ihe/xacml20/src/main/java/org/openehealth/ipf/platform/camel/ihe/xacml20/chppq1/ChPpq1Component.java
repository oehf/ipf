/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq1;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xacml20.CH_PPQ;
import org.openehealth.ipf.commons.ihe.xacml20.audit.ChPpqAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20Endpoint;

import java.util.Map;

/**
 * @since 3.5.1
 * @author Dmytro Rud
 */
public class ChPpq1Component extends AbstractWsComponent<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>, WsInteractionId<WsTransactionConfiguration<ChPpqAuditDataset>>> {

    public ChPpq1Component() {
        super(CH_PPQ.Interactions.CH_PPQ_1);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new Xacml20Endpoint(uri, remaining, this, parameters, ChPpq1Service.class) {
            @Override
            public AbstractWsProducer<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>, ?, ?> getProducer(
                    AbstractWsEndpoint<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>> endpoint,
                    JaxWsClientFactory<ChPpqAuditDataset> clientFactory)
            {
                return new ChPpq1Producer(this, clientFactory);
            }
        };
    }

}
