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
package org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.pid;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7;
import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7Delimiter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Name;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;

/**
 * Transforms a PID-5 conforming string into a {@link PatientInfo} instance. 
 * @author Jens Riemschneider
 */
public class SourcePatientNamePIDTransformer implements PIDTransformer {
    @Override
    public void fromHL7(String hl7Data, PatientInfo patientInfo) {
        notNull(patientInfo, "patientInfo cannot be null");
        
        List<String> parts = HL7.parse(HL7Delimiter.COMPONENT, hl7Data);
        if (parts.isEmpty()) {
            return;
        }
        
        Name name = new Name();
        name.setFamilyName(HL7.get(parts, 1, true));
        name.setGivenName(HL7.get(parts, 2, true));
        name.setSecondAndFurtherGivenNames(HL7.get(parts, 3, true));
        name.setSuffix(HL7.get(parts, 4, true));
        name.setPrefix(HL7.get(parts, 5, true));
        name.setDegree(HL7.get(parts, 6, true));
        
        patientInfo.setName(name);
    }

    @Override
    public String toHL7(PatientInfo patientInfo) {
        notNull(patientInfo, "patientInfo cannot be null");
        
        Name name = patientInfo.getName();
        if (name != null) {
            return HL7.render(HL7Delimiter.COMPONENT, 
                    HL7.escape(name.getFamilyName()),
                    HL7.escape(name.getGivenName()),
                    HL7.escape(name.getSecondAndFurtherGivenNames()),
                    HL7.escape(name.getSuffix()),
                    HL7.escape(name.getPrefix()),
                    HL7.escape(name.getDegree()));
        }
        return null;
    }
}
