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
package org.openehealth.ipf.platform.camel.ihe.xds.iti16.component;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xds.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs21.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiProducer;
import org.openehealth.ipf.platform.camel.ihe.xds.iti16.audit.Iti16ClientAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.iti16.service.Iti16PortType;

/**
 * The producer implementation for the ITI-16 component.
 */
public class Iti16Producer extends DefaultItiProducer<Iti16PortType> {
    /**
     * Constructs the producer.
     * @param endpoint
     *          the endpoint creating this producer.
     * @param serviceInfo
     *          info about the service being called by this producer.
     */
    public Iti16Producer(Iti16Endpoint endpoint, ItiServiceInfo<Iti16PortType> serviceInfo) {
        super(endpoint, serviceInfo);
    }

    @Override
    protected void callService(Exchange exchange) {
        AdhocQueryRequest body =
                exchange.getIn().getBody(AdhocQueryRequest.class);
        RegistryResponse result = getClient().documentRegistryQueryRegistry(body);
        Exchanges.resultMessage(exchange).setBody(result);
    }

    @Override
    public ItiAuditStrategy createAuditStrategy(boolean allowIncompleteAudit) {
        return new Iti16ClientAuditStrategy(allowIncompleteAudit);
    }
}
