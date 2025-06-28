/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CE;

import static org.openehealth.ipf.commons.ihe.xacml20.model.EprConstants.CodingSystemIds;

/**
 * @author Dmytro Rud
 */
@AllArgsConstructor
public enum PurposeOfUse {
    NORMAL    (new CE("NORM",       CodingSystemIds.SWISS_PURPOSE_OF_USE, "eHealth Suisse Verwendungszweck", "Normal access")),
    EMERGENCY (new CE("EMER",       CodingSystemIds.SWISS_PURPOSE_OF_USE, "eHealth Suisse Verwendungszweck", "Emergency access")),
    AUTO      (new CE("AUTO",       CodingSystemIds.SWISS_PURPOSE_OF_USE, "eHealth Suisse Verwendungszweck", "Automatic upload")),
    DICOM_AUTO(new CE("DICOM_AUTO", CodingSystemIds.SWISS_PURPOSE_OF_USE, "eHealth Suisse Verwendungszweck", "Automatic upload of radiological contents"));

    @Getter private final CE code;

    public static PurposeOfUse fromCode(CE code) {
        for (var purposeOfUse : values()) {
            if (purposeOfUse.code.equals(code)) {
                return purposeOfUse;
            }
        }
        return null;
    }
}
