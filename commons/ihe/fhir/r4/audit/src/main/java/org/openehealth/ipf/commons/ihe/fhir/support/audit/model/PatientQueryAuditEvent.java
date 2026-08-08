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
package org.openehealth.ipf.commons.ihe.fhir.support.audit.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Reference;

import static org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpConstants.BALP_PATIENT_QUERY_AUDIT_PROFILE;

@ResourceDef(name = "AuditEvent", id = "PatientQueryAuditEvent", profile = BALP_PATIENT_QUERY_AUDIT_PROFILE)
public class PatientQueryAuditEvent extends QueryAuditEvent {

    /**
     * Sets the patient entity (mandatory)
     * @param patientReference patient reference
     * @return this instance
     */
    public PatientQueryAuditEvent setPatient(Reference patientReference) {
        addPatientEntity(patientReference);
        return this;
    }
}
