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
package org.openehealth.ipf.platform.camel.ihe.xds.iti14.component;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xds.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.ports.Iti14PortType;
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs21.rs.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiProducer;

/**
 * The producer implementation for the ITI-14 component.
 */
public class Iti14Producer extends DefaultItiProducer {
    /**
     * Constructs the producer.
     * @param endpoint
     *          the endpoint creating this producer.
     * @param serviceInfo
     *          info about the service being called by this producer.
     * @param auditStrategy 
     *          the strategy for auditing.
     */
    public Iti14Producer(Iti14Endpoint endpoint, ItiServiceInfo serviceInfo, ItiAuditStrategy auditStrategy) {
        super(endpoint, serviceInfo, auditStrategy);
    }

    @Override
    protected void callService(Exchange exchange) {
        SubmitObjectsRequest body =
                exchange.getIn().getBody(SubmitObjectsRequest.class);
        RegistryResponse result = ((Iti14PortType)getClient()).documentRegistryRegisterDocumentSet(body);
        Exchanges.resultMessage(exchange).setBody(result);
    }
}
