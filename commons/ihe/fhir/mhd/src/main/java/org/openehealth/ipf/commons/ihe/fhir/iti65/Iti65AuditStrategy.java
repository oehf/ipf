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

package org.openehealth.ipf.commons.ihe.fhir.iti65;

import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.DocumentManifest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.fhir.FhirAuditStrategy;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public abstract class Iti65AuditStrategy extends FhirAuditStrategy<Iti65AuditDataset> {

    public Iti65AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public Iti65AuditDataset createAuditDataset() {
        return new Iti65AuditDataset(isServerSide());
    }

    @Override
    public void doAudit(Iti65AuditDataset auditDataset) {
        AuditorManager.getFhirAuditor().auditIti65(
                isServerSide(),
                auditDataset.getEventOutcomeCode(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getClientIpAddress(),
                auditDataset.getPatientId(),
                auditDataset.getDocumentManifestUuid());
    }

    @Override
    public Iti65AuditDataset enrichAuditDatasetFromRequest(Iti65AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        Iti65AuditDataset dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        Bundle bundle = (Bundle) request;
        //
        DocumentManifest documentManifest = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(DocumentManifest.class::isInstance)
                .map(DocumentManifest.class::cast)
                .findFirst().orElseThrow(() -> new RuntimeException("ITI-65 bundle must contain DocumentManifest"));

        dataset.enrichDatasetFromDocumentManifest(documentManifest);
        return dataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(Iti65AuditDataset auditDataset, Object response) {
        Bundle bundle = (Bundle) response;
        // TODO need to extract DocumentManifest (UU)IDs from the response bundle for auditing
        return super.enrichAuditDatasetFromResponse(auditDataset, response);
    }

    /**
     * Look at the response codes in the bundle entries and derive the ATNA event outcome
     * @param resource FHIR resource
     * @return RFC3881EventOutcomeCode
     */
    @Override
    protected RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCodeFromResource(IBaseResource resource) {
        Bundle bundle = (Bundle) resource;
        Set<String> responseStatus = bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResponse)
                .map(Bundle.BundleEntryResponseComponent::getStatus)
                .collect(Collectors.toSet());

        if (responseStatus.stream().anyMatch(s -> s.startsWith("4") || s.startsWith("5"))) {
            return RFC3881EventCodes.RFC3881EventOutcomeCodes.MAJOR_FAILURE;
        };
        return RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS;
    }
}
