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
package org.openehealth.ipf.platform.camel.ihe.xds.iti38.asyncresponse;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.iti38.Iti38ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti38.asyncresponse.Iti38AsyncResponsePortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsAsyncResponseEndpoint;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsComponent;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * The Camel component for the ITI-38 (XCA) async response.
 */
public class Iti38AsyncResponseComponent extends XdsComponent<XdsQueryAuditDataset> {

    private final static WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Service", "ihe"),
            Iti38AsyncResponsePortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Binding", "ihe"),
            false,
            "wsdl/iti38-asyncresponse.wsdl",
            true,
            false,
            false,
            false);

    @Override
    @SuppressWarnings("raw") // Required because of base class
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new XdsAsyncResponseEndpoint<>(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                Iti38AsyncResponseService.class);
    }

    @Override
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public AuditStrategy<XdsQueryAuditDataset> getClientAuditStrategy() {
        return null;   // no producer support
    }

    @Override
    public AuditStrategy<XdsQueryAuditDataset> getServerAuditStrategy() {
        return new Iti38ClientAuditStrategy();
    }

}
