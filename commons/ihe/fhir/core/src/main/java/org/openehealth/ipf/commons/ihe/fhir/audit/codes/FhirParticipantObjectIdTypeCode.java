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

package org.openehealth.ipf.commons.ihe.fhir.audit.codes;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;

import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.EHS_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.IHE_SYSTEM_NAME;

/**
 * ParticipantObjectIdTypeCodes for the FHIR transactions in this module
 *
 * @author Christian Ohr
 */
public enum FhirParticipantObjectIdTypeCode implements ParticipantObjectIdType, EnumeratedCodedValue<ParticipantObjectIdType> {

    ProvideDocumentBundle("ITI-65", IHE_SYSTEM_NAME, "Provide Document Bundle"),
    MobileDocumentManifestQuery("ITI-66", IHE_SYSTEM_NAME, "Mobile Document Manifest Query"),
    MobileDocumentReferenceQuery("ITI-67", IHE_SYSTEM_NAME, "Mobile Document Reference Query"),
    MobileDocumentRetrieval("ITI-68", IHE_SYSTEM_NAME, "Mobile Document Retrieval"),
    MobilePatientDemographicsQuery("ITI-78", IHE_SYSTEM_NAME, "Mobile Patient Demographics Query"),
    RetrieveATNAAuditEvent("ITI-81", IHE_SYSTEM_NAME, "Retrieve ATNA AuditEvent"),
    MobilePatientIdentifierCrossReferenceQuery("ITI-83", IHE_SYSTEM_NAME, "Mobile Patient Identifier Cross-reference Query"),
    PatientDemographicsMatch("ITI-119", IHE_SYSTEM_NAME, "Patient Demographics Match"),
    MobileQueryExistingData("PCC-44", IHE_SYSTEM_NAME, "Mobile Query Existing Data"),
    QueryPharmacyDocumentsOverMhd("PHARM-5", IHE_SYSTEM_NAME, "Query Pharmacy Documents over MHD"),
    MobilePrivacyPolicyQuery("PPQ-5", EHS_SYSTEM_NAME, "Mobile Privacy Policy Query");

    @Getter
    private final ParticipantObjectIdType value;

    FhirParticipantObjectIdTypeCode(String code, String systemName, String displayName) {
        this.value = ParticipantObjectIdType.of(code, systemName, displayName);
    }

    public static ParticipantObjectIdType fromResourceType(String resourceType) {
        return ParticipantObjectIdType.of(
                resourceType,
                "http://hl7.org/fhir/resource-types",
                resourceType);
    }
}


