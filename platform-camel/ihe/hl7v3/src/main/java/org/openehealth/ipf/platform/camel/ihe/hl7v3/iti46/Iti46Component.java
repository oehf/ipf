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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti46;

import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v3.iti46.Iti46AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti46.Iti46PortType;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.AbstractHl7v3WebService;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Endpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;

/**
 * The Camel component for the ITI-46 transaction (PIX v3).
 */
public class Iti46Component extends AbstractWsComponent<Hl7v3WsTransactionConfiguration> {
    private static final String NS_URI = "urn:ihe:iti:pixv3:2007";
    public final static Hl7v3WsTransactionConfiguration WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            IpfInteractionId.ITI_46,
            new QName(NS_URI, "PIXConsumer_Service", "ihe"),
            Iti46PortType.class,
            new QName(NS_URI, "PIXConsumer_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti46/iti46-raw.wsdl",
            "MCCI_IN000002UV01",
            false,
            false,
            false);

    @SuppressWarnings({ "unchecked", "rawtypes" }) // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Hl7v3Endpoint<Hl7v3WsTransactionConfiguration>(uri, remaining, this, getCustomInterceptors(parameters), getFeatures(parameters));
    }

    @Override
    public Hl7v3WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public WsAuditStrategy getClientAuditStrategy(boolean allowIncompleteAudit) {
        return new Iti46AuditStrategy(false, allowIncompleteAudit);
    }

    @Override
    public WsAuditStrategy getServerAuditStrategy(boolean allowIncompleteAudit) {
        return new Iti46AuditStrategy(true, allowIncompleteAudit);
    }

    @Override
    public AbstractHl7v3WebService getServiceInstance(AbstractWsEndpoint<?> endpoint) {
        return new Iti46Service();
    }

    @Override
    public SimpleWsProducer<String, String> getProducer(AbstractWsEndpoint<?> endpoint, JaxWsClientFactory clientFactory) {
        return new SimpleWsProducer<String, String>(endpoint, clientFactory, String.class, String.class);
    }
}
