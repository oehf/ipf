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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.AuditUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpEndpoint;


/**
 * Consumer-side ATNA auditing Camel interceptor.
 * @author Dmytro Rud
 */
public class MllpConsumerAuditInterceptor extends AbstractMllpConsumerInterceptor {
    private static final transient Log LOG = LogFactory.getLog(MllpConsumerAuditInterceptor.class);

    public MllpConsumerAuditInterceptor(MllpEndpoint endpoint, Processor wrappedProcessor) {
        super(endpoint, wrappedProcessor);
    }

    
    /**
     * Performs ATNA auditing. Both input and output messages 
     * are expected to be {@link MessageAdapter}s. 
     * <p>
     * Does not produce any own exceptions, only rethrows exceptions
     * raised during the proper call.
     */
    public void process(Exchange exchange) throws Exception {
        MllpAuditStrategy strategy = getMllpEndpoint().getServerAuditStrategy();
        MllpAuditDataset auditDataset = createAndEnrichAuditDatasetFromRequest(strategy, exchange);

        boolean failed = false;
        try {
            getWrappedProcessor().process(exchange);
            MessageAdapter msg = resultMessage(exchange).getBody(MessageAdapter.class);
            enrichAuditDatasetFromResponse(auditDataset, strategy, msg);
            failed = AuditUtils.isNotPositiveAck(msg);
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

    
    /**
     * Creates a new audit dataset and enriches it with data from the request 
     * message.  All exception are ignored.
     * @return
     *      newly created audit dataset or <code>null</code> when creation failed.
     */
    private MllpAuditDataset createAndEnrichAuditDatasetFromRequest(
            MllpAuditStrategy strategy,
            Exchange exchange) 
    {
        try {
            MessageAdapter msg = exchange.getIn().getBody(MessageAdapter.class);
            MllpAuditDataset auditDataset = strategy.createAuditDataset();
            AuditUtils.enrichGenericAuditDatasetFromSession(auditDataset);
            AuditUtils.enrichGenericAuditDatasetFromRequest(auditDataset, msg);
            strategy.enrichAuditDatasetFromRequest(auditDataset, msg, exchange);
            return auditDataset;
            
        } catch(Exception e) {
            LOG.error("Exception when enriching audit dataset from request", e);
            return null;
        }
    }
    

    /**
     * Enriches the given audit dataset with data from the response message.
     * All exception are ignored.
     */
    private void enrichAuditDatasetFromResponse(
            MllpAuditDataset auditDataset,
            MllpAuditStrategy strategy,
            MessageAdapter msg) 
    {
        try {
            strategy.enrichAuditDatasetFromResponse(auditDataset, msg);
        } catch(Exception e) {
            LOG.error("Exception when enriching audit dataset from response", e);
        }
    }
}
