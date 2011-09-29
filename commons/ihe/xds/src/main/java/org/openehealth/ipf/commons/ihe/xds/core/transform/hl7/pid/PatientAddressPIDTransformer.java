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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.AddressTransformer;

/**
 * Transforms a PID-11 conforming string into a {@link PatientInfo} instance. 
 * @author Jens Riemschneider
 */
public class PatientAddressPIDTransformer implements PIDTransformer {
    private final AddressTransformer addressTransformer = new AddressTransformer();

    @Override
    public void fromHL7(String hl7Data, PatientInfo patientInfo) {
        notNull(patientInfo, "patientInfo cannot be null");
        
        patientInfo.setAddress(addressTransformer.fromHL7(hl7Data));
    }

    @Override
    public String toHL7(PatientInfo patientInfo) {
        notNull(patientInfo, "patientInfo cannot be null");
        
        return addressTransformer.toHL7(patientInfo.getAddress());
    }
}
