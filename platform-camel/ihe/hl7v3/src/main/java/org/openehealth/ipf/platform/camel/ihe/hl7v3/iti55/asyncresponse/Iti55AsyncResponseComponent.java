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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55.asyncresponse;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.asyncresponse.Iti55AsyncResponsePortType;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3AsyncResponseEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import javax.xml.namespace.QName;

/**
 * Camel component for the ITI-55 XCPD Initiating Gateway actor
 * (receivers of asynchronous responses).
 */
public class Iti55AsyncResponseComponent extends AbstractWsComponent<Hl7v3WsTransactionConfiguration> {
    private final static String NS_URI = "urn:ihe:iti:xcpd:2009";
    private final static Hl7v3WsTransactionConfiguration WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            IpfInteractionId.ITI_55,
            new QName(NS_URI, "InitiatingGateway_Service", "xcpd"),
            Iti55AsyncResponsePortType.class,
            new QName(NS_URI, "InitiatingGateway_Binding", "xcpd"),
            false,
            "wsdl/iti55/iti55-asyncresponse-raw.wsdl",
            null,
            false,
            false,
            false);

    @SuppressWarnings("unchecked") // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Hl7v3AsyncResponseEndpoint(uri, remaining, this, getCustomInterceptors(parameters), getFeatures(parameters));
    }

    @Override
    public Hl7v3WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public WsAuditStrategy getClientAuditStrategy(boolean allowIncompleteAudit) {
        return null;   // no producer support
    }

    @Override
    public WsAuditStrategy getServerAuditStrategy(boolean allowIncompleteAudit) {
        return new Iti55AuditStrategy(false, allowIncompleteAudit);
    }

    @Override
    public Iti55AsyncResponseService getServiceInstance(AbstractWsEndpoint<?> endpoint) {
        return new Iti55AsyncResponseService();
    }

    @Override
    public AbstractWsProducer getProducer(
            AbstractWsEndpoint<?> endpoint,
            JaxWsClientFactory clientFactory)
    {
        throw new IllegalStateException("No producer support for asynchronous response endpoints");
    }
}
