/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq4;

import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Consent;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils;

import java.util.Map;

@Slf4j
public abstract class ChPpq4AuditStrategy extends FhirAuditStrategy<ChPpqmAuditDataset> {

    public ChPpq4AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public ChPpqmAuditDataset createAuditDataset() {
        return new ChPpqmAuditDataset(isServerSide());
    }

    @Override
    public ChPpqmAuditDataset enrichAuditDatasetFromRequest(ChPpqmAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        Bundle bundle = (Bundle) request;
        if (!bundle.getEntry().isEmpty()) {
            Bundle.HTTPVerb method = bundle.getEntry().get(0).getRequest().getMethod();
            switch (method) {
                case POST:
                    auditDataset.setAction(EventActionCode.Create);
                    extractConsentIdsFromEntryResources(auditDataset, bundle);
                    break;
                case PUT:
                    auditDataset.setAction(EventActionCode.Update);     // may be changed based on response entry status code
                    extractConsentIdsFromEntryResources(auditDataset, bundle);
                    break;
                case DELETE:
                    auditDataset.setAction(EventActionCode.Delete);
                    auditDataset.getPolicyAndPolicySetIds().addAll(ChPpqmUtils.extractConsentIdsFromEntryUrls(bundle));
                    break;
                default:
                    log.error("Unsupported HTTP method '{}'", method);
            }
        }
        return auditDataset;
    }

    private static void extractConsentIdsFromEntryResources(ChPpqmAuditDataset auditDataset, Bundle bundle) {
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Consent consent = (Consent) entry.getResource();
            auditDataset.getPolicyAndPolicySetIds().add(ChPpqmUtils.extractConsentId(consent, ChPpqmUtils.ConsentIdTypes.POLICY_SET_ID));
        }
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(ChPpqmAuditDataset auditDataset, Object response, AuditContext auditContext) {
        if (super.enrichAuditDatasetFromResponse(auditDataset, response, auditContext)) {
            Bundle bundle = (Bundle) response;
            for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                String status = entry.getResponse().getStatus();
                if (!status.startsWith("2")) {
                    auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.SeriousFailure);
                    break;
                }
                if ((auditDataset.getAction() == EventActionCode.Update) && status.startsWith("201")) {
                    auditDataset.setAction(EventActionCode.Create);
                }
            }
        }
        return (auditDataset.getEventOutcomeIndicator() == EventOutcomeIndicator.Success);
    }

}
