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

package org.openehealth.ipf.commons.ihe.hl7v2.options;


import org.openehealth.ipf.commons.ihe.hl7v2.HL7v2TransactionOption;

import java.util.Arrays;
import java.util.List;


/**
 *
 */
public enum Iti31Options implements HL7v2TransactionOption {

    BASIC("A01", "A03", "A04", "A11", "A13"),
    INPATIENT_OUTPATIENT_ENCOUNTER_MANAGEMENT(BASIC, "A02", "A05", "A06", "A07", "A12", "A38"),
    PENDING_EVENT_MANAGEMENT(INPATIENT_OUTPATIENT_ENCOUNTER_MANAGEMENT, "A14", "A15", "A16", "A25", "A26", "A27"),
    ADVANCED_ENCOUNTER_MANAGEMENT(BASIC, "A21", "A22", "A44", "A52", "A53", "A54", "A55"),
    TEMPORARY_PATIENT_TRANSFERS_TRACKING(BASIC, "A09", "A10", "A32", "A33"),
    HISTORIC_MOVEMENT_MANAGEMENT(INPATIENT_OUTPATIENT_ENCOUNTER_MANAGEMENT, ADVANCED_ENCOUNTER_MANAGEMENT, "Z99"),
    MAINTAIN_DEMOGRAPHICS(BASIC, "A08", "A40");

    private List<String> supportedEvents;

    Iti31Options(String... supportedEvents) {
        this.supportedEvents = Arrays.asList(supportedEvents);
    }

    Iti31Options(Iti31Options option, String... supportedEvents) {
        this.supportedEvents = HL7v2TransactionOption.concat(option, Arrays.asList(supportedEvents));
    }

    Iti31Options(Iti31Options option1, Iti31Options option2, String... supportedEvents) {
        this.supportedEvents = HL7v2TransactionOption.concat(option1, option2, Arrays.asList(supportedEvents));
    }

    @Override
    public List<String> getSupportedThings() {
        return supportedEvents;
    }


}
