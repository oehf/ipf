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

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;

/**
 * Audit dataset of the Patient Identity Feed FHIR [ITI-104] transaction. The identifier of the
 * patient is kept as FHIR token in {@link #getPatientIds()}.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class Iti104AuditDataset extends FhirAuditDataset {

    /**
     * Update or Delete as requested; the Manager records an update that created the patient as Create
     */
    @Getter @Setter
    private EventActionCode action;

    /**
     * Reference of the created, updated or deleted Patient resource (e.g. {@code Patient/123}), if known
     */
    @Getter @Setter
    private String resourceReference;

    public Iti104AuditDataset(boolean serverSide) {
        super(serverSide);
    }
}
