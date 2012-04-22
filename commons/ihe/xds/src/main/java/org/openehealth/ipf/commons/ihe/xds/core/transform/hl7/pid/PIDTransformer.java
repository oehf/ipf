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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;

import java.util.List;

/**
 * Provides transformation for PID-X strings.
 * @author Jens Riemschneider
 */
public interface PIDTransformer {
    /**
     * Transforms the given hl7 data into the patient info. 
     * @param hl7Data
     *          hl7 text to transform. Can be <code>null</code>.
     * @param patientInfo
     *          patient info that receives the data.
     */
    void fromHL7(String hl7Data, PatientInfo patientInfo);

    /**
     * Transforms the given {@link PatientInfo} into HL7 data.
     * @param patientInfo
     *          the {@link PatientInfo} to transform.
     * @return the HL7 data string. <code>null</code> if the resulting 
     *          string would be empty.
     */
    List<String> toHL7(PatientInfo patientInfo);
}
