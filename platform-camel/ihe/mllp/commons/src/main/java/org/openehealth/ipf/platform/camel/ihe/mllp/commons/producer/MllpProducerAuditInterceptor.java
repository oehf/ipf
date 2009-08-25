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
package org.openehealth.ipf.platform.camel.ihe.mllp.commons.producer;

import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.apache.camel.component.mina.MinaExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.AuditUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpEndpoint;


/**
 * Producer-side ATNA auditing Camel interceptor.
 *  
 * @author Dmytro Rud
 */
public class MllpProducerAuditInterceptor extends AbstractMllpProducerInterceptor {
    private static final transient Log LOG = LogFactory.getLog(MllpProducerAuditInterceptor.class);

    /**
     * Constructor.
     */
    public MllpProducerAuditInterceptor(MllpEndpoint endpoint, Producer<MinaExchange> wrappedProducer) {
        super(endpoint, wrappedProducer);
    }
    
    
    /**
     * Performs ATNA auditing. Both input and output messages 
     * are expected to be {@link MessageAdapter}s. 
     * <p>
     * Does not produce any own exceptions, only rethrows exceptions
     * raised during the proper call.
     */
    public void process(Exchange exchange) throws Exception {
        MllpAuditDataset auditDataset = null;
        MllpAuditStrategy strategy = getMllpEndpoint().getClientAuditStrategy();

        try {
            MessageAdapter msg = exchange.getIn().getBody(MessageAdapter.class);
            auditDataset = strategy.createAuditDataset();
            AuditUtils.enrichGenericAuditDatasetFromMessage(auditDataset, msg);
            strategy.enrichAuditDataset(auditDataset, msg);
            auditDataset.setLocalAddress("dummy");   // not used on client side
            auditDataset.setRemoteAddress(
                    AuditUtils.formatEndpointAddress(getEndpoint().getEndpointUri()));
            
        } catch(Exception e) {
            // Ignore all auditing problems, they will be handled later.
            // See URL parameter "allowIncompleteAudit". 
            LOG.error("Exception when preparing the audit dataset", e);
        }
    
        boolean failed = false; 
        try {
            getWrappedProducer().process(exchange);
            
            /*
             * The transaction is considered failed when either:
             *   a) the call has thrown an exception, or
             *   b) the response is not a positive ACK.
             */
            
            failed = exchange.isFailed();
            if( ! failed) {
                MessageAdapter msg = Exchanges.resultMessage(exchange).getBody(MessageAdapter.class);
                failed = AuditUtils.isErrorMessage(msg);
            }
        } catch (Exception e) {
            failed = true;
            throw e;
        } finally {
            AuditUtils.finalizeAudit(
                    auditDataset,
                    getMllpEndpoint().isAllowIncompleteAudit(),
                    strategy, 
                    failed);
        }
    }

    
}
