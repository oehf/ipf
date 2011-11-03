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
package org.openehealth.ipf.platform.camel.ihe.xds.iti17;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

/**
 * The Camel component for the ITI-17 transaction.
 */
public class Iti17Component extends AbstractWsComponent<WsTransactionConfiguration> {
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, @SuppressWarnings("rawtypes") Map parameters) throws Exception {
        return new Iti17Endpoint(uri, remaining, this);
    }

    @Override
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return null;
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
}
