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
package org.openehealth.ipf.commons.ihe.fhir.chppqm.chppq3;

import ca.uhn.fhir.rest.api.MethodOutcome;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Consent;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.chppqm.ChPpqmUtils;

import java.util.Map;

@Slf4j
public abstract class ChPpq3AuditStrategy extends FhirAuditStrategy<ChPpqmAuditDataset> {

    public ChPpq3AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public ChPpqmAuditDataset createAuditDataset() {
        return new ChPpqmAuditDataset(isServerSide());
    }

    @Override
    public ChPpqmAuditDataset enrichAuditDatasetFromRequest(ChPpqmAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        var method = (String) parameters.get(Constants.HTTP_METHOD);
        switch (method) {
            case "POST":
                auditDataset.setAction(EventActionCode.Create);
                enrichAuditDatasetFromConsent(auditDataset, request);
                break;
            case "PUT":
                auditDataset.setAction(EventActionCode.Update);     // may be changed based on MethodOutcome.getCreated()
                enrichAuditDatasetFromConsent(auditDataset, request);
                break;
            case "DELETE":
                auditDataset.setAction(EventActionCode.Delete);
                auditDataset.getPolicyAndPolicySetIds().add((String) request);
                break;
            default:
                log.error("Unsupported HTTP method '{}'", method);
        }

        return auditDataset;
    }

    private static void enrichAuditDatasetFromConsent(ChPpqmAuditDataset auditDataset, Object resource) {
        var consent = (Consent) resource;
        auditDataset.getPolicyAndPolicySetIds().add(ChPpqmUtils.extractConsentId(consent, ChPpqmUtils.ConsentIdTypes.POLICY_SET_ID));
        var pid = consent.getPatient().getIdentifier();
        auditDataset.getPatientIds().add(String.format("%s^^^&%s&ISO", pid.getValue(), pid.getSystem().substring(8)));
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(ChPpqmAuditDataset auditDataset, Object response, AuditContext auditContext) {
        var methodOutcome = (MethodOutcome) response;
        if ((auditDataset.getAction() == EventActionCode.Update) && Boolean.TRUE.equals(methodOutcome.getCreated())) {
            auditDataset.setAction(EventActionCode.Create);
        }

        return super.enrichAuditDatasetFromResponse(auditDataset, response, auditContext);
    }

}
