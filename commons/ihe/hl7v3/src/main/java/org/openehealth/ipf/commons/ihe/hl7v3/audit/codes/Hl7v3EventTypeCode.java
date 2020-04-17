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

package org.openehealth.ipf.commons.ihe.hl7v3.audit.codes;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.EventType;

/**
 * EventTypes for the MLLP transactions in this module#
 *
 * @author Christian Ohr
 * @since 3.5
 */
public enum Hl7v3EventTypeCode implements EventType, EnumeratedCodedValue<EventType> {

    PatientIdentityFeed("ITI-44", "Patient Identity Feed"),
    PIXQuery("ITI-45", "PIX Query"),
    PIXUpdateNotification("ITI-46", "PIX Update Notification"),
    PatientDemographicsQuery("ITI-47", "Patient Demographics Query"),
    CrossGatewayPatientDiscovery("ITI-55", "Cross Gateway Patient Discovery"),
    PatientLocationQuery("ITI-56", "Patient Location Query"),
    QueryExistingData("PCC-1", "Query Existing Data");

    @Getter
    private EventType value;

    Hl7v3EventTypeCode(String code, String displayName) {
        this.value = EventType.of(code, "IHE Transactions", displayName);
    }

}


