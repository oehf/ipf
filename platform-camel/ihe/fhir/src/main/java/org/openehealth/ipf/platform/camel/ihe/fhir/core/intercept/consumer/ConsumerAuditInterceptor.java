/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.consumer;

import org.apache.camel.Exchange;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.FhirObject;
import org.openehealth.ipf.commons.ihe.fhir.atna.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.interceptor.AuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;


/**
 * Consumer-side FHIR ATNA auditing Camel interceptor.
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class ConsumerAuditInterceptor<T extends FhirAuditDataset>
        extends InterceptorSupport<FhirEndpoint<T>> implements AuditInterceptor<T, FhirEndpoint<T>> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerAuditInterceptor.class);

    private final AuditStrategy<T> serverAuditStrategy;

    public ConsumerAuditInterceptor(AuditStrategy<T> serverAuditStrategy) {
        this.serverAuditStrategy = serverAuditStrategy;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        FhirObject msg = exchange.getIn().getBody(FhirObject.class);

        T auditDataset = createAndEnrichAuditDatasetFromRequest(serverAuditStrategy, exchange, msg);
        // determineParticipantsAddresses(interceptor, exchange, auditDataset);

        boolean failed = false;
        try {
            getWrappedProcessor().process(exchange);
            IBaseResource result = resultMessage(exchange).getBody(IBaseResource.class);
            failed = !enrichAuditDatasetFromResponse(getAuditStrategy(), auditDataset, result);
        } catch (Exception e) {
            // FHIR exception or unexpected exception
            failed = true;
            throw e;
        } finally {
            if (auditDataset != null) {
                try {
                    auditDataset.setEventOutcomeCode(failed ?
                            RFC3881EventCodes.RFC3881EventOutcomeCodes.MAJOR_FAILURE :
                            RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS);
                    getAuditStrategy().doAudit(auditDataset);
                } catch (Exception e) {
                    LOG.error("ATNA auditing failed", e);
                }
            }
        }
    }


    @Override
    public AuditStrategy<T> getAuditStrategy() {
        return serverAuditStrategy;
    }

    /**
     * Creates a new audit dataset and enriches it with data from the request
     * message.  All exception are ignored.
     *
     * @return newly created audit dataset or <code>null</code> when creation failed.
     */
    private T createAndEnrichAuditDatasetFromRequest(AuditStrategy<T> strategy, Exchange exchange, FhirObject msg) {
        try {
            T auditDataset = strategy.createAuditDataset();
            // AuditUtils.enrichGenericAuditDatasetFromRequest(auditDataset, msg);
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
    private boolean enrichAuditDatasetFromResponse(AuditStrategy<T> strategy, T auditDataset, IBaseResource response) {
        return strategy.enrichAuditDatasetFromResponse(auditDataset, response);
    }


}
