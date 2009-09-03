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

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7;
import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7Delimiter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.AssigningAuthorityTransformer;

/**
 * Transforms a PID-3 conforming string into a {@link PatientInfo} instance. 
 * @author Jens Riemschneider
 */
public class SourcePatientIdentifierPIDTransformer implements PIDTransformer {
    private final AssigningAuthorityTransformer assigningAuthorityTransformer = 
        new AssigningAuthorityTransformer();

    @Override
    public void fromHL7(String hl7Data, PatientInfo patientInfo) {
        notNull(patientInfo, "patientInfo cannot be null");
        
        List<String> repetitions = HL7.parse(HL7Delimiter.REPETITION, hl7Data);
        for (String hl7id : repetitions) {        
            List<String> parts = HL7.parse(HL7Delimiter.COMPONENT, hl7id);
            
            String idNumber = HL7.get(parts, 1, true);            
            AssigningAuthority assigningAuthority = 
                assigningAuthorityTransformer.fromHL7(HL7.get(parts, 4, false));

            if (idNumber != null || assigningAuthority != null) {
                Identifiable id = new Identifiable();
                id.setId(idNumber);
                id.setAssigningAuthority(assigningAuthority);
            
                patientInfo.getIds().add(id);
            }
        }
    }

    @Override
    public String toHL7(PatientInfo patientInfo) {
        List<String> parts = new ArrayList<String>();
        for (Identifiable id : patientInfo.getIds()) {
            String part = HL7.render(HL7Delimiter.COMPONENT, 
                    HL7.escape(id.getId()),
                    null,
                    null,
                    assigningAuthorityTransformer.toHL7(id.getAssigningAuthority()));
            
            if (part != null) {
                parts.add(part);
            }
        }
        
        return HL7.render(HL7Delimiter.REPETITION, parts.toArray(new String[0]));
    }
}
