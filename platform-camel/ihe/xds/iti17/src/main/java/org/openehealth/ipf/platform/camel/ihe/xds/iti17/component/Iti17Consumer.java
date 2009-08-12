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
package org.openehealth.ipf.platform.camel.ihe.xds.iti17.component;

import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.Auditable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditStrategy;

/**
 * The Camel consumer for the ITI-17 transaction.
 */
public class Iti17Consumer extends DefaultConsumer implements Auditable {
    
    private final Iti17Endpoint endpoint;
    
    /**
     * Constructs the consumer.
     * @param endpoint
     *          the endpoint creating this consumer.
     * @param processor
     *          the processor to start processing incoming exchanges.
     */
    public Iti17Consumer(Iti17Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    /**
     * Processes an incoming download request.
     * @param requestURI
     *          the URI of the download request.
     * @return the content stream for the download.
     * @throws Exception 
     *          any exception that occurred while processing the request.
     */
    public InputStream process(String requestURI) throws Throwable {
        Exchange exchange = Exchanges.createExchange(getEndpoint().getCamelContext(), ExchangePattern.InOut);
        exchange.getIn().setBody(requestURI);
        getProcessor().process(exchange);
        if (exchange.getException() != null) {
            throw exchange.getException();
        }
        return Exchanges.resultMessage(exchange).getBody(InputStream.class);
    }
    
    @Override
    protected void doStart() throws Exception {
        super.doStart();
        endpoint.setActiveConsumer(this);
    }
    
    @Override
    protected void doStop() throws Exception {
        endpoint.setActiveConsumer(null);
        super.doStop();
    }

    @Override
    public AuditStrategy createAuditStrategy(boolean allowIncompleteAudit) {
        // audit strategies are used in Web Service-based transactions only  
        return null;
    }
}