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

import static org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants.CodingSystemIds;

/**
 * Swiss EPR confidentiality code.
 *
 * @author Dmytro Rud
 */
@AllArgsConstructor
public enum ConfidentialityCode {
    NORMAL    (new CE("1051000195109", CodingSystemIds.SNOMED_CT, "SNOMED Clinical Terms", "Normal")),
    RESTRICTED(new CE("1131000195104", CodingSystemIds.SNOMED_CT, "SNOMED Clinical Terms", "Restricted")),
    SECRET    (new CE("1141000195107", CodingSystemIds.SNOMED_CT, "SNOMED Clinical Terms", "Secret"));

    @Getter private final CE code;
}
