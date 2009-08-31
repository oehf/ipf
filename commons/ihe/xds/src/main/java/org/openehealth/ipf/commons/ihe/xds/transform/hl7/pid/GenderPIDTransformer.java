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
package org.openehealth.ipf.commons.ihe.xds.transform.hl7.pid;

import static org.apache.commons.lang.Validate.notNull;

import org.openehealth.ipf.commons.ihe.xds.metadata.PatientInfo;

/**
 * Transforms a PID-8 conforming string into a {@link PatientInfo} instance. 
 * @author Jens Riemschneider
 */
public class GenderPIDTransformer implements PIDTransformer {
    @Override
    public void fromHL7(String hl7Data, PatientInfo patientInfo) {
        notNull(patientInfo, "patientInfo cannot be null");

        patientInfo.setGender(hl7Data == null || hl7Data.isEmpty() ? null : hl7Data);
    }

    @Override
    public String toHL7(PatientInfo patientInfo) {
        notNull(patientInfo, "patientInfo cannot be null");
        
        return patientInfo.getGender();
    }
}
