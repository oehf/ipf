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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti56;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v3.iti56.Iti56AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti56.Iti56PortType;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Component;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Endpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * The Camel component for the ITI-56 transaction (XCPD).
 */
public class Iti56Component extends Hl7v3Component<Hl7v3WsTransactionConfiguration> {
    private final static String NS_URI = "urn:ihe:iti:xcpd:2009";
    public final static Hl7v3WsTransactionConfiguration WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            IpfInteractionId.ITI_56,
            new QName(NS_URI, "RespondingGateway_Service", "xcpd"),
            Iti56PortType.class,
            new QName(NS_URI, "RespondingGateway_Binding_Soap12", "xcpd"),
            false,
            "wsdl/iti56/iti56-raw.wsdl",
            null,
            null,
            true,
            true);

    @SuppressWarnings({"raw", "unchecked"}) // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Hl7v3Endpoint<Hl7v3WsTransactionConfiguration>(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                Iti56Service.class) {
            @Override
            public AbstractWsProducer getProducer(AbstractWsEndpoint<Hl7v3AuditDataset, Hl7v3WsTransactionConfiguration> endpoint,
                                                  JaxWsClientFactory<Hl7v3AuditDataset> clientFactory) {
                return new SimpleWsProducer<>(endpoint, clientFactory, String.class, String.class);
            }
        };
    }

    @Override
    public Hl7v3WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public AuditStrategy<Hl7v3AuditDataset> getClientAuditStrategy() {
        return new Iti56AuditStrategy(false);
    }

    @Override
    public AuditStrategy<Hl7v3AuditDataset> getServerAuditStrategy() {
        return new Iti56AuditStrategy(true);
    }

}
