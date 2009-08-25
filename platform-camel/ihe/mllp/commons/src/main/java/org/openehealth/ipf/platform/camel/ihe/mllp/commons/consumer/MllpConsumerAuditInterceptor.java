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
package org.openehealth.ipf.platform.camel.ihe.mllp.commons.consumer;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.mina.MinaExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.openehealth.ipf.modules.hl7.AckTypeCode;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.AuditUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpMarshalUtils;


/**
 * Consumer-side ATNA auditing Camel interceptor.
 * 
 * @author Dmytro Rud
 */
public class MllpConsumerAuditInterceptor extends AbstractMllpConsumerInterceptor {
    private static final transient Log LOG = LogFactory.getLog(MllpConsumerAuditInterceptor.class);

    public MllpConsumerAuditInterceptor(MllpEndpoint endpoint, Processor wrappedProcessor) {
        super(endpoint, wrappedProcessor);
    }

    public void process(Exchange exchange) throws Exception {
        MllpAuditDataset auditDataset = null;
        MllpAuditStrategy strategy = getMllpEndpoint().getServerAuditStrategy();
        
        // create and fill an ATNA audit dataset
        try {
            MessageAdapter msg = exchange.getIn().getBody(MessageAdapter.class);
            auditDataset = strategy.createAuditDataset();
            
            // transaction-agnostic enrichment
            IoSession session = ((MinaExchange) exchange).getSession();
            AuditUtils.enrichGenericAuditDatasetFromSession(auditDataset, session);
            AuditUtils.enrichGenericAuditDatasetFromMessage(auditDataset, msg);
            // transaction-specific enrichment
            strategy.enrichAuditDataset(auditDataset, msg);
            
        } catch(Exception e) {
            // Ignore all auditing problems, they will be handled later.
            // See URL parameter "allowIncompleteAudit". 
            LOG.error("Exception when preparing the audit dataset", e);
        }

        boolean failed = false;
        try {
            getWrappedProcessor().process(exchange);
            Object body = resultMessage(exchange).getBody();
            
            /*
             * The transaction is considered failed when either:
             *   a) the route has thrown an exception, or
             *   b) the exchange contains an exception object in the body, or
             *   c) the type of the exchange body is not supported
             *      and the "magic header" does not denote success.
             */
            Object header = resultMessage(exchange).getHeader(MllpComponent.ACK_TYPE_CODE_HEADER);
            failed = exchange.isFailed() || 
                     (body instanceof Exception) ||
                     ( ! (MllpMarshalUtils.typeSupported(body) || (header == AckTypeCode.AA)));
            
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
