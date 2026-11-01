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
package org.openehealth.ipf.commons.ihe.hl7v3.core.metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.ihe.gazelle.hl7v3.datatypes.CE;

/**
 * Simplified model of a patient returned in a patient demographics query response (PDQv3 ITI-47, XCPD ITI-55),
 * i.e. the content of one {@code registrationEvent} in a PRPA_IN201306UV02 message.
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MatchedPatient extends Patient {

    /**
     * The degree of match ({@code subjectOf1/queryMatchObservation/value}), between 0 and 100.
     */
    private Integer matchDegree;

    /**
     * The custodian code ({@code registrationEvent/custodian/assignedEntity/code}). XCPD (ITI-55) uses this to
     * indicate Health Data Locator support; if not set, the XCPD transformer emits {@code NotHealthDataLocator}.
     */
    private CE custodianCode;
}
