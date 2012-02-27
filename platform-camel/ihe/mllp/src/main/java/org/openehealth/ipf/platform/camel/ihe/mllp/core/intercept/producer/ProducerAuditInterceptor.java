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

import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AbstractMllpInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AuditInterceptorUtils;

import java.net.InetAddress;


/**
 * Producer-side ATNA auditing Camel interceptor.
 * @author Dmytro Rud
 */
public class ProducerAuditInterceptor extends AbstractMllpInterceptor implements AuditInterceptor {

    @Override
    public void process(Exchange exchange) throws Exception {
        AuditInterceptorUtils.doProcess(this, exchange);
    }

    
    @Override
    public void determineParticipantsAddresses(
            Exchange exchange,
            MllpAuditDataset auditDataset) throws Exception 
    {
        auditDataset.setLocalAddress(InetAddress.getLocalHost().getCanonicalHostName());
        auditDataset.setRemoteAddress(getMllpEndpoint().getEndpointUri());
    }


    @Override
    public MllpAuditStrategy getAuditStrategy() {
        return getMllpEndpoint().getClientAuditStrategy();
    }
}
