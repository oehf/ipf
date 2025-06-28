/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xacml20.chadr;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CE;

/**
 * Swiss EPR document confidentiality codes
 */
@RequiredArgsConstructor
public enum ConfidentialityCode {

    NORMAL(new CE("17621005", "2.16.840.1.113883.6.96", null, "Normal")),
    RESTRICTED(new CE("263856008", "2.16.840.1.113883.6.96", null, "Restricted")),
    SECRET(new CE("1141000195107", "2.16.756.5.30.1.127.3.4", null, "Secret"));

    @Getter
    private final CE code;

}
