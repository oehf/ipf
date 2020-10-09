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

package org.openehealth.ipf.commons.ihe.fhir.iti81;

import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Bundle;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.support.OperationOutcomeOperations;

/**
 * ITI-81 audit strategy
 *
 * @author Christian Ohr
 * @since 3.6
 */
public class Iti81AuditStrategy extends FhirAuditStrategy<FhirAuditEventQueryAuditDataset> {

    public Iti81AuditStrategy(boolean serverSide) {
        super(serverSide, OperationOutcomeOperations.INSTANCE);
    }

    @Override
    public FhirAuditEventQueryAuditDataset createAuditDataset() {
        return new FhirAuditEventQueryAuditDataset(isServerSide());
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, FhirAuditEventQueryAuditDataset auditDataset) {
        return new IHEAuditLogUsedBuilder(auditContext, auditDataset).getMessages();
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(FhirAuditEventQueryAuditDataset auditDataset, Object response, AuditContext auditContext) {
        var bundle = (Bundle) response;
        bundle.getEntry().stream()
                        .filter(bundleEntryComponent -> bundleEntryComponent.getResource() instanceof AuditEvent)
                        .map(Bundle.BundleEntryComponent::getFullUrl)
                        .forEach(uri -> auditDataset.getAuditEventUris().add(uri));
        return super.enrichAuditDatasetFromResponse(auditDataset, response, auditContext);
    }
}
