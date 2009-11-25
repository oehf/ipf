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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.mina.MinaEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuthenticationFailure;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AbstractMllpInterceptor;

import java.net.InetSocketAddress;

/**
 * Interceptor that handles any {@link MllpAuthenticationFailure} that occurred while
 * processing an exchange.
 */
public class ConsumerAuthenticationFailureInterceptor extends AbstractMllpInterceptor {
    private MllpAuditStrategy serverAuditStrategy;

    /**
     * Constructs the interceptor.
     * @param endpoint
     *      The Camel endpoint to which this interceptor belongs.
     * @param wrappedProcessor
     *      Original camel-mina processor.
     * @param serverAuditStrategy
     *      Audit strategy to log authentication failures.
     */
    public ConsumerAuthenticationFailureInterceptor(MllpEndpoint endpoint, Processor wrappedProcessor, MllpAuditStrategy serverAuditStrategy) {
        super(endpoint, wrappedProcessor);
        this.serverAuditStrategy = serverAuditStrategy;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            getWrappedProcessor().process(exchange);
        }
        catch (MllpAuthenticationFailure e) {
            serverAuditStrategy.auditAuthenticationNodeFailure(getRemoteAddress(exchange));
            throw e;
        }
    }

    private String getRemoteAddress(Exchange exchange) {
        InetSocketAddress address = (InetSocketAddress) exchange.getIn().getHeader(MinaEndpoint.HEADER_REMOTE_ADDRESS);
        return address.getAddress().getHostAddress();
    }

}
