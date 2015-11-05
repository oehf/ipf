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
import org.openehealth.ipf.commons.ihe.fhir.FhirObject;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirComponent;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.AbstractFhirInterceptor;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.AuditInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;


/**
 * Consumer-side ATNA auditing Camel interceptor.
 *
 * @author Dmytro Rud
 */
public class ConsumerAuditInterceptor<
        AuditDatasetType extends FhirAuditDataset,
        ComponentType extends FhirComponent<AuditDatasetType>>

        extends AbstractFhirInterceptor<AuditDatasetType, ComponentType>
        implements AuditInterceptor<AuditDatasetType, ComponentType> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerAuditInterceptor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        FhirObject msg = exchange.getIn().getBody(FhirObject.class);

        // pass in case of non-auditable message types
        // if (!isAuditable(msg)) {
        //     getWrappedProcessor().process(exchange);
        //     return;
        // }

        AuditDatasetType auditDataset = createAndEnrichAuditDatasetFromRequest(getAuditStrategy(), exchange, msg);
        // determineParticipantsAddresses(interceptor, exchange, auditDataset);

        boolean failed = false;
        try {
            getWrappedProcessor().process(exchange);
            FhirObject result = resultMessage(exchange).getBody(FhirObject.class);
            enrichAuditDatasetFromResponse(getAuditStrategy(), auditDataset, msg);
            // failed = !AuditUtils.isPositiveAck(result);
        } catch (Exception e) {
            failed = true;
            throw e;
        } finally {
            // AuditUtils.finalizeAudit(auditDataset, getAuditStrategy(), failed);
        }
    }

    @Override
    public FhirAuditStrategy<AuditDatasetType> getAuditStrategy() {
        return getFhirEndpoint().getServerAuditStrategy();
    }

    /**
     * Creates a new audit dataset and enriches it with data from the request
     * message.  All exception are ignored.
     *
     * @return newly created audit dataset or <code>null</code> when creation failed.
     */
    private AuditDatasetType createAndEnrichAuditDatasetFromRequest(
            FhirAuditStrategy<AuditDatasetType> strategy,
            Exchange exchange,
            FhirObject msg) {
        try {
            AuditDatasetType auditDataset = strategy.createAuditDataset();
            // AuditUtils.enrichGenericAuditDatasetFromRequest(auditDataset, msg);
            strategy.enrichAuditDatasetFromRequest(auditDataset, msg, exchange);
            return auditDataset;
        } catch (Exception e) {
            LOG.error("Exception when enriching audit dataset from request", e);
            return null;
        }
    }


    /**
     * Enriches the given audit dataset with data from the response message.
     * All exception are ignored.
     */
    private void enrichAuditDatasetFromResponse(
            FhirAuditStrategy<AuditDatasetType> strategy,
            AuditDatasetType auditDataset,
            FhirObject msg) {
        try {
            strategy.enrichAuditDatasetFromResponse(auditDataset, msg);
        } catch (Exception e) {
            LOG.error("Exception when enriching audit dataset from response", e);
        }
    }


}
