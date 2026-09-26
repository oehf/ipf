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

/**
 * Constants of the Patient Identity Feed FHIR [ITI-104] transaction.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public interface Iti104Constants {

    /**
     * Search parameter of the conditional update or delete that identifies the patient.
     */
    String IDENTIFIER_PARAMETER = "identifier";

    /**
     * Name of the Camel header by which a Patient Identity Source may state the identifier of the
     * conditional update or delete, either as {@link org.hl7.fhir.r4.model.Identifier} or as
     * {@code system|value} token. If missing, the identifier is taken from the message body.
     */
    String ITI104_PATIENT_IDENTIFIER_HEADER = "Iti104PatientIdentifier";
}
