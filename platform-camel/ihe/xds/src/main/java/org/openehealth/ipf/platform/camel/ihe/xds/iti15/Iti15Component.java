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
package org.openehealth.ipf.platform.camel.ihe.xds.iti15;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.iti15.Iti15ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti15.Iti15PortType;
import org.openehealth.ipf.commons.ihe.xds.iti15.Iti15ServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsComponent;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsEndpoint;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * The Camel component for the ITI-15 transaction.
 */
public class Iti15Component extends XdsComponent<XdsSubmitAuditDataset> {
    private final static WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds:2007", "DocumentRepository_Service", "ihe"),
            Iti15PortType.class,
            new QName("urn:ihe:iti:xds:2007", "DocumentRepository_Binding_Soap11", "ihe"),
            false,
            "wsdl/iti15.wsdl",
            false,
            true,
            false,
            false);

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return new XdsEndpoint<XdsSubmitAuditDataset>(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                Iti15Service.class) {
            @Override
            public AbstractWsProducer<XdsSubmitAuditDataset, WsTransactionConfiguration, ?, ?> getProducer(AbstractWsEndpoint<XdsSubmitAuditDataset, WsTransactionConfiguration> endpoint,
                                                  JaxWsClientFactory<XdsSubmitAuditDataset> clientFactory) {
                return new Iti15Producer(endpoint, clientFactory);
            }
        };
    }

    @Override
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public AuditStrategy<XdsSubmitAuditDataset> getClientAuditStrategy() {
        return new Iti15ClientAuditStrategy();
    }

    @Override
    public AuditStrategy<XdsSubmitAuditDataset> getServerAuditStrategy() {
        return new Iti15ServerAuditStrategy();
    }


}
