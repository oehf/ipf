/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.producer;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.interceptor.AuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.consumer.AuditInterceptorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;


/**
 * Consumer-side FHIR ATNA auditing Camel interceptor.
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class ProducerAuditInterceptor<AuditDatasetType extends FhirAuditDataset>
        extends InterceptorSupport
        implements AuditInterceptor<AuditDatasetType> {

    private static final Logger log = LoggerFactory.getLogger(ProducerAuditInterceptor.class);

    private final AuditContext auditContext;

    public ProducerAuditInterceptor(AuditContext auditContext) {
        this.auditContext = requireNonNull(auditContext);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        var msg = exchange.getIn().getBody();

        AuditDatasetType auditDataset = null;
        try {
            auditDataset = createAndEnrichAuditDatasetFromRequest(getAuditStrategy(), exchange, msg);
            determineParticipantsAddresses(exchange, auditDataset);
        } catch (Exception e) {
            log.error("Error while enriching audit dataset from request", e);
        }

        // Pass in AuditDataset for Client Interceptor
        exchange.getIn().setHeader(Constants.FHIR_AUDIT_HEADER, auditDataset);

        var failed = false;
        try {
            getWrappedProcessor().process(exchange);
            if (auditDataset != null) {
                try {
                    var result = exchange.getMessage().getBody();
                    failed = !enrichAuditDatasetFromResponse(getAuditStrategy(), auditDataset, result);
                    AuditInterceptorUtils.enrichAuditDatasetFromResponse(auditDataset, auditContext, exchange);
                } catch (Exception e) {
                    log.error("Error while enriching audit dataset from response", e);
                }
            }
        } catch (Exception e) {
            // FHIR exception or unexpected exception
            failed = true;
            throw e;
        } finally {
            if (auditDataset != null) {
                auditDataset.setEventOutcomeIndicator(failed ?
                        EventOutcomeIndicator.MajorFailure :
                        EventOutcomeIndicator.Success);
                getAuditStrategy().doAudit(auditContext, auditDataset);
            }
        }
    }

    @Override
    public void determineParticipantsAddresses(Exchange exchange, AuditDatasetType auditDataset) {
    }

    @Override
    public AuditStrategy<AuditDatasetType> getAuditStrategy() {
        return getEndpoint(FhirEndpoint.class).getClientAuditStrategy();
    }

    /**
     * Creates a new audit dataset and enriches it with data from the request
     * message.  All exception are ignored.
     *
     * @return newly created audit dataset or <code>null</code> when creation failed.
     */
    private AuditDatasetType createAndEnrichAuditDatasetFromRequest(AuditStrategy<AuditDatasetType> strategy, Exchange exchange, Object msg) {
        try {
            var auditDataset = strategy.createAuditDataset();
            auditDataset.setSourceUserId(auditContext.getAuditValueIfMissing());
            AuditInterceptorUtils.enrichAuditDatasetFromRequest(auditDataset, auditContext, exchange);
            return strategy.enrichAuditDatasetFromRequest(auditDataset, msg, exchange.getIn().getHeaders());
        } catch (Exception e) {
            log.error("Exception when enriching audit dataset from request", e);
            return null;
        }
    }


    /**
     * Enriches the given audit dataset with data from the response message.
     * All exception are ignored.
     */
    private boolean enrichAuditDatasetFromResponse(AuditStrategy<AuditDatasetType> strategy, AuditDatasetType auditDataset, Object response) {
        return strategy.enrichAuditDatasetFromResponse(auditDataset, response, auditContext);
    }


}
