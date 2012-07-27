/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.ws.mbean;

import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

public class SomeItiComponent extends AbstractWsComponent<WsTransactionConfiguration> {

    private static final String NS_URI = "urn:iti:some:mai:2011";
    public static final WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
            new QName(NS_URI, "SomeConsumer_Service", "iti"),
            String.class,
            new QName(NS_URI, "SomeConsumer_Binding_Soap12", "iti"),
            false,
            "wsdl/some/some.wsdl",
            true,
            false,
            false,
            false);
    
    @Override
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public WsAuditStrategy getClientAuditStrategy(boolean allowIncompleteAudit) {
        return null;   // dummy
    }

    @Override
    public WsAuditStrategy getServerAuditStrategy(boolean allowIncompleteAudit) {
        return null;   // dummy
    }

    @Override
    public AbstractWebService getServiceInstance(AbstractWsEndpoint<?> endpoint) {
        return null;   // dummy
    }

    @Override
    public AbstractWsProducer getProducer(AbstractWsEndpoint<?> endpoint, JaxWsClientFactory clientFactory) {
        return null;   // dummy
    }

    @SuppressWarnings("unchecked")
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new SomeItiEndpoint(uri, remaining, this, getCustomInterceptors(parameters), getFeatures(parameters));
    }

}
