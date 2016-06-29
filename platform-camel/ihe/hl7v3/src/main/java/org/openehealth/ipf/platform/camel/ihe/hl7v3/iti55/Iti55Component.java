/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55PortType;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Component;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Endpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * The Camel component for the ITI-55 transaction (XCPD).
 */
public class Iti55Component extends Hl7v3Component<Hl7v3WsTransactionConfiguration> {
    private final static String NS_URI = "urn:ihe:iti:xcpd:2009";
    public final static Hl7v3WsTransactionConfiguration WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            IpfInteractionId.ITI_55,
            new QName(NS_URI, "RespondingGateway_Service", "xcpd"),
            Iti55PortType.class,
            new QName(NS_URI, "RespondingGateway_Binding_Soap12", "xcpd"),
            false,
            "wsdl/iti55/iti55-raw.wsdl",
            "PRPA_IN201306UV02",
            "PRPA_TE201306UV02",
            false,
            true);


    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return new Hl7v3Endpoint<Hl7v3WsTransactionConfiguration>(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                null) {
            @Override
            public AbstractWsProducer<Hl7v3AuditDataset, Hl7v3WsTransactionConfiguration, ?, ?> getProducer(AbstractWsEndpoint<Hl7v3AuditDataset, Hl7v3WsTransactionConfiguration> endpoint,
                                                  JaxWsClientFactory<Hl7v3AuditDataset> clientFactory) {
                return new Iti55Producer(endpoint, clientFactory);
            }

            @Override
            protected AbstractWebService getCustomServiceInstance(AbstractWsEndpoint<Hl7v3AuditDataset, Hl7v3WsTransactionConfiguration> endpoint) {
                return new Iti55Service((Hl7v3Endpoint<Hl7v3WsTransactionConfiguration>) endpoint);
            }
        };
    }

    @Override
    public Hl7v3WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public AuditStrategy<Hl7v3AuditDataset> getClientAuditStrategy() {
        return new Iti55AuditStrategy(false);
    }

    @Override
    public AuditStrategy<Hl7v3AuditDataset> getServerAuditStrategy() {
        return new Iti55AuditStrategy(true);
    }

}
