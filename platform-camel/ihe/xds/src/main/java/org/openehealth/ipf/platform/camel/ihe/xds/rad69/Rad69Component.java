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
package org.openehealth.ipf.platform.camel.ihe.xds.rad69;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveImagingDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.rad69.Rad69AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.rad69.Rad69PortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsEndpoint;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * The Camel component for the RAD-69 transaction.
 */
public class Rad69Component extends AbstractWsComponent<WsTransactionConfiguration> {
    private final static WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:rad:xdsi-b:2009", "DocumentRepository_Service", "iherad"),
            Rad69PortType.class,
            new QName("urn:ihe:rad:xdsi-b:2009", "DocumentRepository_Binding_Soap12", "iherad"),
            true,
            "wsdl/rad69.wsdl",
            true,
            false,
            false,
            false);

    @Override
    @SuppressWarnings("unchecked") // Required because of base class
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new XdsEndpoint(uri, remaining, this, getCustomInterceptors(parameters), null);
    }

    @Override
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public WsAuditStrategy getClientAuditStrategy(boolean allowIncompleteAudit) {
        return new Rad69AuditStrategy(false, allowIncompleteAudit);
    }

    @Override
    public WsAuditStrategy getServerAuditStrategy(boolean allowIncompleteAudit) {
        return new Rad69AuditStrategy(true, allowIncompleteAudit);
    }

    @Override
    public Rad69Service getServiceInstance(AbstractWsEndpoint<?> endpoint) {
        return new Rad69Service();
    }

    @Override
    public SimpleWsProducer<RetrieveImagingDocumentSetRequestType, RetrieveDocumentSetResponseType> getProducer(
            AbstractWsEndpoint<?> endpoint,
            JaxWsClientFactory clientFactory)
    {
        return new SimpleWsProducer<RetrieveImagingDocumentSetRequestType, RetrieveDocumentSetResponseType>(
                endpoint, clientFactory, RetrieveImagingDocumentSetRequestType.class, RetrieveDocumentSetResponseType.class);
    }
}
