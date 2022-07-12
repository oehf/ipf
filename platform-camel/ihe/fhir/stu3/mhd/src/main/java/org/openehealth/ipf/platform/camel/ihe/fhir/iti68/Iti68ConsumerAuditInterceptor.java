/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.iti68;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti68.Iti68AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.interceptor.AuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.consumer.AuditInterceptorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Audit Interceptor for ITI-68. Note that the ParticipantObjectIdentificationType for the document
 * is not populated automatically, because the location of the parametzers depends upon the actual
 * implementation. The AuditDataset is forwarded in the Camel message header "AuditDataset" so that
 * at least {@link Iti68AuditDataset#documentUniqueId} can be set.
 *
 * @author Christian Ohr
 * @since 3.5
 */
class Iti68ConsumerAuditInterceptor
        extends InterceptorSupport
        implements AuditInterceptor<Iti68AuditDataset> {

    private static final Logger LOG = LoggerFactory.getLogger(Iti68ConsumerAuditInterceptor.class);

    private final AuditContext auditContext;

    Iti68ConsumerAuditInterceptor(AuditContext auditContext) {
        this.auditContext = auditContext;
    }

    @Override
    public AuditStrategy<Iti68AuditDataset> getAuditStrategy() {
        return getEndpoint(Iti68Endpoint.class).getServerAuditStrategy();
    }

    @Override
    public void determineParticipantsAddresses(Exchange exchange, Iti68AuditDataset auditDataset) {
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        var auditDataset = createAndEnrichAuditDatasetFromRequest(getAuditStrategy(), exchange, exchange.getIn().getBody());
        determineParticipantsAddresses(exchange, auditDataset);

        // Add audit dataset to the exchange headers so that a processing route could add things like
        // document id, repository id etc. This is because this information may be present (in case of
        // FHIR/XDS bridges) in the request URI, but there is no definition HOW.
        exchange.getIn().setHeader(AUDIT_DATASET_HEADER, auditDataset);

        var failed = false;
        try {
            getWrappedProcessor().process(exchange);
            failed = exchange.isFailed();
            // Do not check the body, because it's a stream
        } catch (Exception e) {
            if (auditDataset != null) {
                auditDataset.setEventOutcomeDescription(e.getMessage());
            }
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

    /**
     * Creates a new audit dataset and enriches it with data from the request
     * message.  All exception are ignored.
     *
     * @return newly created audit dataset or <code>null</code> when creation failed.
     */
    private Iti68AuditDataset createAndEnrichAuditDatasetFromRequest(AuditStrategy<Iti68AuditDataset> strategy, Exchange exchange, Object msg) {
        try {
            var auditDataset = strategy.createAuditDataset();

            var request = exchange.getIn().getHeader(Exchange.HTTP_SERVLET_REQUEST, HttpServletRequest.class);
            auditDataset.setSourceUserId("unknown");
            auditDataset.setDestinationUserId(request.getRequestURL().toString());
            auditDataset.setRemoteAddress(request.getRemoteAddr());

            // TODO Also extract basic auth user?
            AuditInterceptorUtils.extractClientCertificateCommonName(exchange, auditDataset);

            return strategy.enrichAuditDatasetFromRequest(auditDataset, msg, exchange.getIn().getHeaders());
        } catch (Exception e) {
            LOG.error("Exception when enriching audit dataset from request", e);
            return null;
        }
    }

}
