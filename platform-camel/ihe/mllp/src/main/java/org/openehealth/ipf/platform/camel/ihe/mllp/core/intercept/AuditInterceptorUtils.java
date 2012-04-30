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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AuditUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;


/**
 * Generic functionality for PIX/PDQ auditing interceptors, 
 * a kind of Template Method.
 * @author Dmytro Rud
 */
public class AuditInterceptorUtils  {
    private static final transient Log LOG = LogFactory.getLog(AuditInterceptorUtils.class);

    private AuditInterceptorUtils() {
        throw new IllegalStateException("Helper class");
    }

    
    /**
     * Performs ATNA auditing. Both input and output messages 
     * are expected to be {@link MessageAdapter}s. 
     * <p>
     * Does not produce any own exceptions, only rethrows exceptions
     * raised during the proper call.
     */
    public static void doProcess(AuditInterceptor interceptor, Exchange exchange) throws Exception {
        MessageAdapter<?> msg = exchange.getIn().getBody(MessageAdapter.class);

        // pass in case of non-auditable message types
        if( ! isAuditable(interceptor, msg)) {
            interceptor.getWrappedProcessor().process(exchange);
            return;
        }
        
        MllpAuditStrategy strategy = interceptor.getAuditStrategy();
        MllpAuditDataset auditDataset = 
            createAndEnrichAuditDatasetFromRequest(strategy, exchange, msg);
        determineParticipantsAddresses(interceptor, exchange, auditDataset);

        boolean failed = false;
        try {
            interceptor.getWrappedProcessor().process(exchange);
            msg = resultMessage(exchange).getBody(MessageAdapter.class);
            enrichAuditDatasetFromResponse(strategy, auditDataset, msg);
            failed = ! AuditUtils.isPositiveAck(msg);
        } catch (Exception e) {
            failed = true;
            throw e;
        } finally {
            AuditUtils.finalizeAudit(
                    auditDataset,
                    interceptor.getMllpEndpoint().isAllowIncompleteAudit(),
                    strategy,
                    failed);
        }
    }

    
    /**
     * Checks whether the given message should be audited.
     * All exceptions are ignored. 
     */
    private static boolean isAuditable(AuditInterceptor interceptor, MessageAdapter<?> msg) {
        try {
            Message message = msg.getHapiMessage();
            Terser terser = new Terser(message);
            
            // no audit for fragments 2..n
            if (ArrayUtils.contains(message.getNames(), "DSC") && StringUtils.isNotEmpty(terser.get("DSC-1"))) {
                return false;
            }
            
            String messageType = terser.get("MSH-9-1");
            return interceptor.getMllpEndpoint().getHl7v2TransactionConfiguration().isAuditable(messageType);
        } catch (Exception e) {
            LOG.error("Exception when determining message auditability, no audit will be performed", e);
            return false;
        }
    }
    
    
    /**
     * Creates a new audit dataset and enriches it with data from the request 
     * message.  All exception are ignored.
     * @return
     *      newly created audit dataset or <code>null</code> when creation failed.
     */
    private static MllpAuditDataset createAndEnrichAuditDatasetFromRequest(
            MllpAuditStrategy strategy,
            Exchange exchange,
            MessageAdapter<?> msg)
    {
        try {
            MllpAuditDataset auditDataset = strategy.createAuditDataset();
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
    private static void enrichAuditDatasetFromResponse(
            MllpAuditStrategy strategy,
            MllpAuditDataset auditDataset,
            MessageAdapter<?> msg) 
    {
        try {
            strategy.enrichAuditDatasetFromResponse(auditDataset, msg);
        } catch(Exception e) {
            LOG.error("Exception when enriching audit dataset from response", e);
        }
    }


    /**
     * Determines addresses of local and remote participants and stores them 
     * into the audit dataset.  All exception are ignored.
     */
    private static void determineParticipantsAddresses(
            AuditInterceptor interceptor,
            Exchange exchange, 
            MllpAuditDataset auditDataset) 
    {
        try {
            interceptor.determineParticipantsAddresses(exchange, auditDataset);
        } catch(Exception e) {
            LOG.error("Exception when determining participants' addresses", e);
        }
    }
}
