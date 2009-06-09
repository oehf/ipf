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
package org.openehealth.ipf.platform.camel.ihe.xds.iti41.component;

import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiProducer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.ihe.xds.iti41.audit.Iti41ClientAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.iti41.service.Iti41PortType;
import org.openehealth.ipf.platform.camel.ihe.xds.iti41.service.ProvideAndRegisterDocumentSetRequestType;

/**
 * The producer implementation for the ITI-41 component.
 */
public class Iti41Producer extends DefaultItiProducer<Iti41PortType> {
    
    /**
     * Constructs the producer.
     * @param endpoint
     *          the endpoint creating this producer.
     * @param serviceInfo
     *          info about the service being called by this producer.
     */
    public Iti41Producer(Iti41Endpoint endpoint, ItiServiceInfo<Iti41PortType> serviceInfo) {
        super(endpoint, serviceInfo);
    }

    
    @Override
    protected void callService(Exchange exchange) {
        ProvideAndRegisterDocumentSetRequestType body =
                exchange.getIn().getBody(ProvideAndRegisterDocumentSetRequestType.class);
        RegistryResponseType result = getClient().documentRepositoryProvideAndRegisterDocumentSetB(body);
        Exchanges.resultMessage(exchange).setBody(result);
    }

    
    @Override
    public AuditStrategy createAuditStrategy() {
        return new Iti41ClientAuditStrategy();
    }
}