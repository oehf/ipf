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

package org.openehealth.ipf.commons.ihe.core.atna.codes;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.EventId;

/**
 * Custom IHE Event IDs
 *
 * @author Christian Ohr
 * @since 3.5
 */
public enum IHEEventIdCode implements EventId, EnumeratedCodedValue<EventId> {

    /**
     * Health services scheduled and performed within
     * an instance or episode of care. This includes
     * scheduling, initiation, updates or amendments,
     * performing or completing the act, and
     * cancellation.
     */
    HealthServicesProvisionEvent("IHE0001", "Health Services Provision Event"),
    /**
     * Medication orders and administration within an
     * instance or episode of care. This includes initial
     * order, dispensing, delivery, and cancellation
     */
    MedicationEvent("IHE0002", "Medication Event"),
    /**
     * Staffing or participant assignment actions
     * relevant to the assignment of healthcare
     * professionals, caregivers attending physician,
     * residents, medical students, consultants, etc. to a
     * patient It also includes change in assigned role or
     * authorization, e.g., relative to healthcare status
     * change, and de-assignment
     */
    PatientCareResourceAssignment("IHE0003", "Patient Care ResourceAssignment"),
    /**
     * Specific patient care episodes or problems that
     * occur within an instance of care. This includes
     * initial assignment, updates or amendments,
     * resolution, completion, and cancellation.
     */
    PatientCareEpisode("IHE0004", "Patient Care Episode"),
    /**
     * Patient association with a care protocol. This
     * includes initial assignment, scheduling, updates
     * or amendments, completion, and cancellation.
     */
    PatientCareProtocol("IHE0005", "Patient Care Protocol");

    @Getter
    private EventId value;

    IHEEventIdCode(String code, String displayName) {
        this.value = EventId.of(code, "IHE", displayName);
    }

}
