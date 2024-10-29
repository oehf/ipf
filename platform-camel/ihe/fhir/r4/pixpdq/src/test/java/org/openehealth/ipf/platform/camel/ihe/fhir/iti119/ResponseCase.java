/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.fhir.iti119;

import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.codesystems.MatchGrade;

import java.math.BigDecimal;
import java.util.List;

import static ca.uhn.fhir.model.api.ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE;
import static ca.uhn.fhir.model.api.ResourceMetadataKeyEnum.ENTRY_SEARCH_SCORE;
import static ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum.MATCH;
import static org.openehealth.ipf.commons.ihe.fhir.iti119.AdditionalResourceMetadataKeyEnum.ENTRY_MATCH_GRADE;

public enum ResponseCase {

    OK;

    public List<Patient> populateResponse(Parameters request) {
        var patient = new Patient()
            .addName(new HumanName().setFamily("Test"));
        patient.setId("Patient/4711");
        ENTRY_SEARCH_MODE.put(patient, MATCH);
        ENTRY_SEARCH_SCORE.put(patient, new BigDecimal(String.valueOf(0.95d)));
        ENTRY_MATCH_GRADE.put(patient, MatchGrade.PROBABLE);
        return List.of(patient);
    }
}
