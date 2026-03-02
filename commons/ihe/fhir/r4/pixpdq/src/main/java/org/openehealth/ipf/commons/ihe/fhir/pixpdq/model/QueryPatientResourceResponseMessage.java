/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.pixpdq.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Pdqm320;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmProfile;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Bundle for ITI-78 Mobile Patient Demographics Query Response as defined by the PDQm specification.
 * This is a searchset Bundle containing Patient resources that match the query parameters.
 *
 * @author Christian Ohr
 * @since 5.2
 */
@ResourceDef(name = "Bundle", profile = PdqmProfile.ITI78_QUERY_PATIENT_RESOURCE_RESPONSE_MESSAGE_PROFILE)
public class QueryPatientResourceResponseMessage extends Bundle implements Pdqm320 {

    private static final Predicate<BundleEntryComponent> IS_PDQ_PATIENT_ENTRY =
        entry -> entry.hasResource() && entry.getResource() instanceof PdqmPatient;


    public QueryPatientResourceResponseMessage() {
        super();
        setType(BundleType.SEARCHSET);
        setId(UUID.randomUUID().toString());
        PdqmProfile.ITI78_QUERY_PATIENT_RESOURCE_RESPONSE_MESSAGE.setProfile(this);
    }

    @Override
    public QueryPatientResourceResponseMessage copy() {
        var dst = new QueryPatientResourceResponseMessage();
        copyValues(dst);
        return dst;
    }

    public void copyValues(QueryPatientResourceResponseMessage dst) {
        super.copyValues(dst);
    }

    /**
     * Get all patients from the response bundle
     *
     * @return list of Patient resources
     */
    public List<Patient> getPatients() {
        return getEntry() == null ? Collections.emptyList() :
            getEntry().stream()
                .filter(IS_PDQ_PATIENT_ENTRY)
                .map(entry -> (Patient) entry.getResource())
                .collect(Collectors.toList());
    }

    /**
     * Check if the response contains any patients
     *
     * @return true if there are patients in the response
     */
    public boolean hasPatients() {
        return getEntry() != null && getEntry().stream()
            .anyMatch(IS_PDQ_PATIENT_ENTRY);
    }

    /**
     * Get the number of patients in the response
     *
     * @return count of patients
     */
    public int getPatientCount() {
        return getEntry() == null ? 0 :
            (int) getEntry().stream()
                .filter(IS_PDQ_PATIENT_ENTRY)
                .count();
    }

    /**
     * Add a patient to the response bundle
     *
     * @param patient the PdqmPatient to add
     * @return this bundle for method chaining
     */
    public QueryPatientResourceResponseMessage addPatient(PdqmPatient patient) {
        var entry = addEntry();
        entry.setResource(patient);
        // Use the patient's ID for the fullUrl if it's already a proper URL/URN
        String patientId = patient.getId();
        if (patientId.startsWith("http://") || patientId.startsWith("https://") || patientId.startsWith("urn:")) {
            entry.setFullUrl(patientId);
        } else {
            // Otherwise create a URN from it
            entry.setFullUrl("urn:uuid:" + patient.getIdElement().getIdPart());
        }
        entry.getSearch().setMode(Bundle.SearchEntryMode.MATCH);
        // Update total count
        setTotal(getPatientCount());
        return this;
    }
}