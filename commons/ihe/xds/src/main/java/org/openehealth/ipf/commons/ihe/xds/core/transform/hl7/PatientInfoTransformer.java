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
package org.openehealth.ipf.commons.ihe.xds.core.transform.hl7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7;
import org.openehealth.ipf.commons.ihe.xds.core.hl7.HL7Delimiter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.pid.DateOfBirthPIDTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.pid.GenderPIDTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.pid.PIDTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.pid.PatientAddressPIDTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.pid.SourcePatientIdentifierPIDTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.pid.SourcePatientNamePIDTransformer;

/**
 * Transformation logic for a {@link PatientInfo}.
 * <p>
 * This class offers transformation between {@link PatientInfo} and an HL7v2.5
 * PID string list.
 * @author Jens Riemschneider
 */
public class PatientInfoTransformer {
    private static final String PID_PREFIX = "PID-";
    
    private static final Map<Integer, PIDTransformer> pidTransformers;
    
    static {
        pidTransformers = new HashMap<Integer, PIDTransformer>();
        pidTransformers.put(3, new SourcePatientIdentifierPIDTransformer());
        pidTransformers.put(5, new SourcePatientNamePIDTransformer());
        pidTransformers.put(7, new DateOfBirthPIDTransformer());
        pidTransformers.put(8, new GenderPIDTransformer());
        pidTransformers.put(11, new PatientAddressPIDTransformer());
    }

    /**
     * Creates a {@link PatientInfo} instance via an HL7 XCN string.
     * @param hl7PID
     *          the HL7 PID strings. Can be <code>null</code>.
     * @return the created {@link PatientInfo} instance. <code>null</code> if no relevant 
     *          data was found in the HL7 string or the input was <code>null</code>.
     */
    public PatientInfo fromHL7(List<String> hl7PID) {
        if (hl7PID == null || hl7PID.isEmpty()) {
            return null;
        }
        
        PatientInfo patientInfo = new PatientInfo();
        
        for (String hl7PIDLine : hl7PID) {
            List<String> fields = HL7.parse(HL7Delimiter.FIELD, hl7PIDLine);
            if (fields.size() == 2) {
                String pidNoStr = fields.get(0);
                Integer pidNo = getPidNumber(pidNoStr);
                PIDTransformer transformer = pidTransformers.get(pidNo);
                if (transformer != null) {            
                    transformer.fromHL7(fields.get(1), patientInfo);
                }
            }
        }
        
        return patientInfo;
    }
    
    /**
     * Transforms a {@link PatientInfo} instance into the HL7 representation. 
     * @param patientInfo
     *          the patient info to transform. Can be <code>null</code>.
     * @return the HL7 representation. <code>null</code> if the input was <code>null</code> 
     *          or the resulting HL7 string would be empty.
     */
    public List<String> toHL7(PatientInfo patientInfo) {
        if (patientInfo == null) {
            return Collections.emptyList();
        }
        
        List<String> hl7Strings = new ArrayList<String>();
        for (Map.Entry<Integer, PIDTransformer> entry : pidTransformers.entrySet()) {
            String hl7Data = entry.getValue().toHL7(patientInfo);
            if (hl7Data != null && !hl7Data.isEmpty()) {
                List<String> repetitions = HL7.parse(HL7Delimiter.REPETITION, hl7Data);
                for (String repetition : repetitions) {
                    String pidNoStr = PID_PREFIX + entry.getKey();
                    String pidStr = HL7.render(HL7Delimiter.FIELD, pidNoStr, repetition);
                    hl7Strings.add(pidStr);
                }
            }
        }
        
        return hl7Strings;
    }

    private Integer getPidNumber(String pidNoStr) {
        if (!pidNoStr.startsWith(PID_PREFIX)) {
            return null;
        }
        
        try {
            return Integer.parseInt(pidNoStr.substring(PID_PREFIX.length()));                
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
}
