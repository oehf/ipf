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
package org.openehealth.ipf.platform.camel.ihe.xds.iti18.component;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xds.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.ports.Iti18PortType;
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiProducer;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

/**
 * The producer implementation for the ITI-18 component.
 */
public class Iti18Producer extends DefaultItiProducer {
    /**
     * Constructs the producer.
     * @param endpoint
     *          the endpoint creating this producer.
     * @param serviceInfo
     *          info about the service being called by this producer.
     * @param auditStrategy 
     *          the strategy for auditing to use.
     */
    public Iti18Producer(Iti18Endpoint endpoint, ItiServiceInfo serviceInfo, ItiAuditStrategy auditStrategy) {
        super(endpoint, serviceInfo, auditStrategy);
    }
    
    @Override
    protected void callService(Exchange exchange) {
        AdhocQueryRequest body =
                exchange.getIn().getBody(AdhocQueryRequest.class);
        AdhocQueryResponse result = ((Iti18PortType) getClient()).documentRegistryRegistryStoredQuery(body);
        Exchanges.resultMessage(exchange).setBody(result);
    }
}
