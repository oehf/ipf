/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v3.core.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.ihe.gazelle.hl7v3.datatypes.II;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplified model of a "Patient Registry Duplicates Resolved" message (PRPA_IN201304UV02), used by the
 * Patient Identity Feed HL7v3 (ITI-44) to merge patients. The {@link #getPatient() patient} is the surviving one.
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PatientDuplicatesResolvedRequest extends PatientRecordRequest {

    /**
     * The IDs of the patient that was merged into the surviving patient
     * ({@code replacementOf/priorRegistration/subject1/priorRegisteredRole/id}). At least one ID shall be provided.
     * The list is never {@code null}.
     */
    private final List<II> obsoletePatientIds = new ArrayList<>();
}
