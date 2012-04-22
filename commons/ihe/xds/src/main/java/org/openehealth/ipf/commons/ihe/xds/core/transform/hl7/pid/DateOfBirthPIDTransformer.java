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

import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;

import java.util.Collections;
import java.util.List;

/**
 * Transforms a PID-7 conforming string into a {@link PatientInfo} instance. 
 * @author Jens Riemschneider
 */
public class DateOfBirthPIDTransformer implements PIDTransformer {
    @Override
    public void fromHL7(String hl7Data, PatientInfo patientInfo) {
        notNull(patientInfo, "patientInfo cannot be null");

        if (StringUtils.isEmpty(hl7Data)) {
            return;
        }
        int pos = hl7Data.indexOf('^');
        patientInfo.setDateOfBirth((pos > 0) ? hl7Data.substring(0, pos) : hl7Data);
    }

    @Override
    public List<String> toHL7(PatientInfo patientInfo) {
        notNull(patientInfo, "patientInfo cannot be null");
        String date = patientInfo.getDateOfBirth();
        return StringUtils.isEmpty(date) ? null : Collections.singletonList(date);
    }
}
