/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.pid;

import static org.apache.commons.lang.Validate.notNull;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7Delimiter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.PatientInfo;

/**
 * Transforms a PID-7 conforming string into a {@link PatientInfo} instance. 
 * @author Jens Riemschneider
 */
public class DateOfBirthPIDTransformer implements PIDTransformer {
    @Override
    public void fromHL7(String hl7Data, PatientInfo patientInfo) {
        notNull(patientInfo, "patientInfo cannot be null");
        
        List<String> parts = HL7.parse(HL7Delimiter.COMPONENT, hl7Data);
        patientInfo.setDateOfBirth(HL7.get(parts, 1, true));
    }

    @Override
    public String toHL7(PatientInfo patientInfo) {
        notNull(patientInfo, "patientInfo cannot be null");

        return HL7.escape(patientInfo.getDateOfBirth());
    }
}
