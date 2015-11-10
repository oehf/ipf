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
package org.openehealth.ipf.platform.camel.ihe.xds.iti63;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.iti63.Iti63AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti63.Iti63PortType;
import org.openehealth.ipf.platform.camel.ihe.ws.*;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsComponent;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsEndpoint;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * The Camel component for the ITI-63 transaction.
 */
public class Iti63Component extends XdsComponent<XdsQueryAuditDataset> {

    private final static WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Service", "ihe"),
            Iti63PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Binding_Soap12", "ihe"),
            true,
            "wsdl/iti63.wsdl",
            true,
            false,
            true,
            true);

    @Override
    @SuppressWarnings("unchecked") // Required because of base class
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new XdsEndpoint<XdsQueryAuditDataset>(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                null) {
            @Override
            public AbstractWsProducer getProducer(AbstractWsEndpoint<XdsQueryAuditDataset, WsTransactionConfiguration> endpoint,
                                                  JaxWsClientFactory<XdsQueryAuditDataset> clientFactory) {
                return new SimpleWsProducer<>(
                        endpoint, clientFactory, AdhocQueryRequest.class, AdhocQueryResponse.class);
            }

            @Override
            protected <T extends AbstractWebService> T getCustomServiceInstance(AbstractWsEndpoint<XdsQueryAuditDataset, WsTransactionConfiguration> endpoint) {
                return (T) new Iti63Service(endpoint);
            }
        };
    }

    @Override
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public AuditStrategy<XdsQueryAuditDataset> getClientAuditStrategy() {
        return new Iti63AuditStrategy(false);
    }

    @Override
    public AuditStrategy<XdsQueryAuditDataset> getServerAuditStrategy() {
        return new Iti63AuditStrategy(true);
    }

}
