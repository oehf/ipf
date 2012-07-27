/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.iti39.asyncresponse;

import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti39.Iti39ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti39.asyncresponse.Iti39AsyncResponsePortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsAsyncResponseEndpoint;

/**
 * The Camel component for the ITI-39 (XCA) async response.
 */
public class Iti39AsyncResponseComponent extends AbstractWsComponent<WsTransactionConfiguration> {
    private final static WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Service", "ihe"),
            Iti39AsyncResponsePortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Binding", "ihe"),
            false,
            "wsdl/iti39-asyncresponse.wsdl",
            true,
            false,
            false,
            false);

    @SuppressWarnings("unchecked") // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new XdsAsyncResponseEndpoint(uri, remaining, this, getCustomInterceptors(parameters), getFeatures(parameters));
    }

    @Override
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public WsAuditStrategy getClientAuditStrategy(boolean allowIncompleteAudit) {
        return null;   // no producer support
    }

    @Override
    public WsAuditStrategy getServerAuditStrategy(boolean allowIncompleteAudit) {
        return new Iti39ClientAuditStrategy(allowIncompleteAudit);
    }

    @Override
    public Iti39AsyncResponseService getServiceInstance(AbstractWsEndpoint<?> endpoint) {
        return new Iti39AsyncResponseService();
    }

    @Override
    public AbstractWsProducer<?,?> getProducer(
            AbstractWsEndpoint<?> endpoint,
            JaxWsClientFactory clientFactory)
    {
        throw new IllegalStateException("No producer support for asynchronous response endpoints");
    }
}
