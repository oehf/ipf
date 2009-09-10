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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer;

import java.net.InetAddress;

import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AuditInterceptorUtils;


/**
 * Producer-side ATNA auditing Camel interceptor.
 * @author Dmytro Rud
 */
public class ProducerAuditInterceptor 
        extends AbstractProducerInterceptor 
        implements AuditInterceptor
{
    public ProducerAuditInterceptor(MllpEndpoint endpoint, Producer wrappedProducer) {
        super(endpoint, wrappedProducer);
    }
    
    
    public void process(Exchange exchange) throws Exception {
        AuditInterceptorUtils.doProcess(this, exchange);
    }

    
    public void determineParticipantsAddresses(
            Exchange exchange,
            MllpAuditDataset auditDataset) throws Exception 
    {
        auditDataset.setLocalAddress(InetAddress.getLocalHost().getCanonicalHostName());
        auditDataset.setRemoteAddress(getMllpEndpoint().getEndpointUri().substring(6));
    }


    public MllpAuditStrategy getAuditStrategy() {
        return getMllpEndpoint().getClientAuditStrategy();
    }
}
