/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.xacml20.audit.codes;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.EventType;

/**
 * EventTypes for transactions in this module
 *
 * @since 3.5.1
 */
public enum Xacml20EventTypeCodes implements EventType, EnumeratedCodedValue<EventType> {
    PrivacyPolicyFeed             ("PPQ-1",  CODE_SYSTEM_NAME_EHS,  "Privacy Policy Feed"),
    PrivacyPolicyRetrieve         ("PPQ-2",  CODE_SYSTEM_NAME_EHS,  "Privacy Policy Retrieve"),
    AuthorizationDecisionsQueryIhe("ITI-79", CODE_SYSTEM_NAME_IHE_TRANSACTIONS, "Authorization Decisions Query"),
    AuthorizationDecisionsQueryAdr("ADR",    CODE_SYSTEM_NAME_EHS,  "Authorization Decisions Query"),
    ;

    @Getter
    private final EventType value;

    Xacml20EventTypeCodes(String code, String codeSystemName, String displayName) {
        this.value = EventType.of(code, codeSystemName, displayName);
    }

}


