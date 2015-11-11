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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55.deferredresponse;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.asyncresponse.Iti55DeferredResponsePortType;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3AsyncResponseEndpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Component;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * Camel component for the ITI-55 XCPD Initiating Gateway actor
 * (receivers of deferred responses).
 */
public class Iti55DeferredResponseComponent extends Hl7v3Component<Hl7v3WsTransactionConfiguration> {
    public static final String THREAD_POOL_NAME = "iti55.deferred.response";

    private final static String NS_URI = "urn:ihe:iti:xcpd:2009";
    private final static Hl7v3WsTransactionConfiguration WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            IpfInteractionId.ITI_55,
            new QName(NS_URI, "InitiatingGateway_Service", "xcpd"),
            Iti55DeferredResponsePortType.class,
            new QName(NS_URI, "InitiatingGatewayDeferredResponse_Binding", "xcpd"),
            false,
            "wsdl/iti55/iti55-deferred-response-raw.wsdl",
            null,
            null,
            false,
            false);

    @SuppressWarnings("raw") // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Hl7v3AsyncResponseEndpoint<Hl7v3WsTransactionConfiguration>(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                Iti55DeferredResponseService.class) {
            @Override
            public AbstractWsProducer getProducer(AbstractWsEndpoint<Hl7v3AuditDataset, Hl7v3WsTransactionConfiguration> endpoint,
                                                  JaxWsClientFactory<Hl7v3AuditDataset> clientFactory) {
                return new Iti55DeferredResponseProducer(endpoint, clientFactory);
            }
        };
    }

    @Override
    public Hl7v3WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public AuditStrategy<Hl7v3AuditDataset> getClientAuditStrategy() {
        return null;   // producers send responses, so the server-side strategy must be used
    }

    @Override
    public AuditStrategy<Hl7v3AuditDataset> getServerAuditStrategy() {
        return new Iti55AuditStrategy(false);
    }

}
