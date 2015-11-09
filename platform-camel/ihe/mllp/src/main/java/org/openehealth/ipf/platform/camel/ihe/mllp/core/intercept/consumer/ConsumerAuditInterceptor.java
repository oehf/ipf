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

import java.net.InetSocketAddress;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.mina2.Mina2Constants;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.MllpAuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AuditInterceptorUtils;


/**
 * Consumer-side ATNA auditing Camel interceptor.
 * @author Dmytro Rud
 */
public class ConsumerAuditInterceptor<T extends MllpAuditDataset>
        extends InterceptorSupport<MllpTransactionEndpoint<T>>
        implements MllpAuditInterceptor<T>
{
    @Override
    public void process(Exchange exchange) throws Exception {
        AuditInterceptorUtils.doProcess(this, exchange);
    }

    @Override
    public AuditStrategy<T> getAuditStrategy() {
        return getEndpoint().getServerAuditStrategy();
    }

    @Override
    public void determineParticipantsAddresses(Exchange exchange, MllpAuditDataset auditDataset) throws Exception {
        Message message = exchange.getIn();
        auditDataset.setLocalAddress(addressFromHeader(message, Mina2Constants.MINA_LOCAL_ADDRESS));
        auditDataset.setRemoteAddress(addressFromHeader(message, Mina2Constants.MINA_REMOTE_ADDRESS));
    }
    
    /**
     * Extracts string representation of IP address from socket address instance  
     * stored in the given header of the given Camel message.  
     */
    private static String addressFromHeader(Message message, String headerName) {
        InetSocketAddress address = (InetSocketAddress) message.getHeader(headerName);
        return address.getAddress().getHostAddress();
    }
    
}
