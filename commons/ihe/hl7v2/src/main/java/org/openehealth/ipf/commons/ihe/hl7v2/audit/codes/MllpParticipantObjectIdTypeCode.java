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

package org.openehealth.ipf.commons.ihe.hl7v2.audit.codes;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;

/**
 * Participant Object ID Types for the MLLP transactions in this module
 *
 * @author Christian Ohr
 * @since 3.5
 */
public enum MllpParticipantObjectIdTypeCode implements ParticipantObjectIdType, EnumeratedCodedValue<ParticipantObjectIdType> {

    PatientIdentityFeed("ITI-8", "Patient Identity Feed"),
    PIXQuery("ITI-9", "PIX Query"),
    PIXUpdateNotification("ITI-10", "PIX Update Notification"),
    PatientDemographicsQuery("ITI-21", "Patient Demographics Query"),
    PatientDemographicsAndVisitQuery("ITI-22", "Patient Demographics and Visit Query"),
    PatientDemographicsSupplier("ITI-30", "Patient Identity Management"),

    SubmissionSet("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd", "submission set classificationNode"),
    RegistryObjectReference("urn:ihe:iti:2017:ObjectRef", "registry object reference");


    @Getter
    private final ParticipantObjectIdType value;

    MllpParticipantObjectIdTypeCode(String code, String displayName) {
        this.value = ParticipantObjectIdType.of(code, CODE_SYSTEM_NAME_IHE_TRANSACTIONS, displayName);
    }

}


