/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.svs.core.audit;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.EventType;

/**
 * EventTypes for the SVS module
 *
 * @author Quentin Ligier
 */
public enum SvsEventTypeCode implements EventType, EnumeratedCodedValue<EventType> {

    RetrieveValueSet("ITI-48", "Retrieve Value Set");

    @Getter
    private final EventType value;

    SvsEventTypeCode(String code, String displayName) {
        this.value = EventType.of(code, CODE_SYSTEM_NAME_IHE_TRANSACTIONS, displayName);
    }
}