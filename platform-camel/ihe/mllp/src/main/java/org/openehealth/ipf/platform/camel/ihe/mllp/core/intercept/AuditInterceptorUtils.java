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

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.AuditUtils;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.MllpAuditDataset;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.ihe.atna.interceptor.AuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;


/**
 * Generic functionality for PIX/PDQ auditing interceptors,
 * a kind of Template Method.
 *
 * @author Dmytro Rud
 */
public final class AuditInterceptorUtils {
    private static final transient Logger LOG = LoggerFactory.getLogger(AuditInterceptorUtils.class);

    private AuditInterceptorUtils() {
        throw new IllegalStateException("Helper class");
    }


    /**
     * Performs ATNA auditing. Both input and output messages
     * are expected to be {@link Message}s.
     * <p/>
     * Does not produce any own exceptions, only rethrows exceptions
     * raised during the proper call.
     */
    public static <AuditDatasetType extends MllpAuditDataset> void doProcess(
            AuditInterceptor<AuditDatasetType, MllpTransactionEndpoint<AuditDatasetType>> interceptor,
            Exchange exchange) throws Exception {
        Message msg = exchange.getIn().getBody(Message.class);

        // pass in case of non-auditable message types
        if (!isAuditable(interceptor, msg)) {
            interceptor.getWrappedProcessor().process(exchange);
            return;
        }

        AuditStrategy<AuditDatasetType> strategy = interceptor.getAuditStrategy();
        AuditDatasetType auditDataset = createAndEnrichAuditDatasetFromRequest(strategy, exchange, msg);
        determineParticipantsAddresses(interceptor, exchange, auditDataset);

        boolean failed = false;
        try {
            interceptor.getWrappedProcessor().process(exchange);
            Message result = resultMessage(exchange).getBody(Message.class);
            // TODO: Shouldn't this be result instead of msg?? See https://github.com/oehf/ipf/issues/124
            enrichAuditDatasetFromResponse(strategy, auditDataset, msg);
            failed = !AuditUtils.isPositiveAck(result);
        } catch (Exception e) {
            failed = true;
            throw e;
        } finally {
            AuditUtils.finalizeAudit(auditDataset, strategy, failed);
        }
    }


    /**
     * Checks whether the given message should be audited.
     * All exceptions are ignored.
     */
    private static <AuditDatasetType extends MllpAuditDataset> boolean isAuditable(AuditInterceptor<AuditDatasetType, MllpTransactionEndpoint<AuditDatasetType>> interceptor, Message message) {
        try {
            Terser terser = new Terser(message);

            // no audit for fragments 2..n
            if (ArrayUtils.contains(message.getNames(), "DSC") && StringUtils.isNotEmpty(terser.get("DSC-1"))) {
                return false;
            }

            return interceptor.getEndpoint().getHl7v2TransactionConfiguration().isAuditable(MessageUtils.eventType(message));
        } catch (Exception e) {
            LOG.error("Exception when determining message auditability, no audit will be performed", e);
            return false;
        }
    }


    /**
     * Creates a new audit dataset and enriches it with data from the request
     * message.  All exception are ignored.
     *
     * @return newly created audit dataset or <code>null</code> when creation failed.
     */
    private static <T extends MllpAuditDataset> T createAndEnrichAuditDatasetFromRequest(
            AuditStrategy<T> strategy,
            Exchange exchange,
            Message msg) {
        try {
            T auditDataset = strategy.createAuditDataset();
            AuditUtils.enrichGenericAuditDatasetFromRequest(auditDataset, msg);
            return strategy.enrichAuditDatasetFromRequest(auditDataset, msg, exchange.getIn().getHeaders());
        } catch (Exception e) {
            LOG.error("Exception when enriching audit dataset from request", e);
            return null;
        }
    }


    /**
     * Enriches the given audit dataset with data from the response message.
     * All exception are ignored.
     */
    private static <AuditDatasetType extends MllpAuditDataset> void enrichAuditDatasetFromResponse(
            AuditStrategy<AuditDatasetType> strategy,
            AuditDatasetType auditDataset,
            Message msg) {
        try {
            strategy.enrichAuditDatasetFromResponse(auditDataset, msg);
        } catch (Exception e) {
            LOG.error("Exception when enriching audit dataset from response", e);
        }
    }


    /**
     * Determines addresses of local and remote participants and stores them
     * into the audit dataset.  All exception are ignored.
     */
    private static <AuditDatasetType extends MllpAuditDataset> void determineParticipantsAddresses(
            AuditInterceptor<AuditDatasetType, MllpTransactionEndpoint<AuditDatasetType>> interceptor,
            Exchange exchange,
            AuditDatasetType auditDataset) {
        try {
            interceptor.determineParticipantsAddresses(exchange, auditDataset);
        } catch (Exception e) {
            LOG.error("Exception when determining participants' addresses", e);
        }
    }
}
