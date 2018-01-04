/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit.codes;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;

/**
 * Purpose of Use codes from XSPA
 */
public enum XspaPoUCode implements PurposeOfUse, EnumeratedCodedValue<PurposeOfUse> {

    Treatment("TREATMENT", "Healthcare Treatment"),
    Payment("PAYMENT", "Payment"),
    Operations("OPERATIONS", "Operations"),
    Emergency("EMERGENCY", "Emergency Treatment"),
    Sysadmin("SYSADMIN", "System Administration"),
    Research("RESEARCH", "Research"),
    Marketing("MARKETING", "Marketing"),
    Request("REQUEST", "Request of the Individual"),
    PublicHealth("PUBLICHEALTH", "Public Health");


    @Getter
    private PurposeOfUse value;

    XspaPoUCode(String code, String displayName) {
        this.value = PurposeOfUse.of(code, "XSPA", displayName);
    }

}


