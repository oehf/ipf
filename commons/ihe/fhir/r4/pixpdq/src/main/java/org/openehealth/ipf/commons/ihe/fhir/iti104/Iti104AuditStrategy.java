/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti104;

import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.BalpPatientRecordBuilder;

import java.util.Map;

/**
 * Audit strategy for the Patient Identity Feed FHIR [ITI-104] transaction. It records a DICOM
 * Patient Record event, from which the AuditEvents of the PIXm Feed audit profiles can be derived.
 * <p>
 * An update is recorded as Update by the Patient Identity Source, which cannot tell whether it has
 * created the patient, and as Create or Update by the Patient Identifier Cross-reference Manager,
 * depending on {@link MethodOutcome#getCreated()}.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class Iti104AuditStrategy extends FhirAuditStrategy<Iti104AuditDataset> {

    public Iti104AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public Iti104AuditDataset createAuditDataset() {
        return new Iti104AuditDataset(isServerSide());
    }

    @Override
    public Iti104AuditDataset enrichAuditDatasetFromRequest(Iti104AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        var dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        dataset.setAction(request instanceof Patient ? EventActionCode.Update : EventActionCode.Delete);
        Iti104Utils.patientIdentifier(request, parameters)
            .map(Iti104Utils::toToken)
            .ifPresent(dataset.getPatientIds()::add);
        if (request instanceof Patient patient && patient.getIdElement().hasIdPart()) {
            dataset.setResourceReference(patient.getIdElement().toUnqualifiedVersionless().getValue());
        }
        return dataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(Iti104AuditDataset auditDataset, Object response, AuditContext auditContext) {
        if (response instanceof MethodOutcome methodOutcome) {
            if (methodOutcome.getId() != null && methodOutcome.getId().hasIdPart()) {
                auditDataset.setResourceReference(methodOutcome.getId().toUnqualifiedVersionless().getValue());
            }
            if (isServerSide() && auditDataset.getAction() == EventActionCode.Update && Boolean.TRUE.equals(methodOutcome.getCreated())) {
                auditDataset.setAction(EventActionCode.Create);
            }
        }
        return super.enrichAuditDatasetFromResponse(auditDataset, response, auditContext);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, Iti104AuditDataset auditDataset) {
        return new BalpPatientRecordBuilder(auditContext, auditDataset, auditDataset.getAction(),
            FhirEventTypeCode.MobilePatientIdentityFeed, auditDataset.getPatientId())
            // for a conditional delete, the resource may only be known by its condition
            .addDataEntity(auditDataset.getResourceReference() != null ?
                auditDataset.getResourceReference() :
                conditionalReference(auditDataset.getPatientId()))
            .getMessages();
    }

    private static String conditionalReference(String patientId) {
        return patientId != null ?
            "Patient?" + Iti104Constants.IDENTIFIER_PARAMETER + "=" + patientId :
            null;
    }
}
