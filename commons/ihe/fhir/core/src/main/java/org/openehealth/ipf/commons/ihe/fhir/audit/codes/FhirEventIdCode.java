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

package org.openehealth.ipf.commons.ihe.fhir.audit.codes;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.EventId;

/**
 * @author Christian Ohr
 */
public enum FhirEventIdCode implements EventId, EnumeratedCodedValue<EventId> {

    RestfulOperation("rest", "RESTful Operation");

    @Getter
    private final EventId value;

    FhirEventIdCode(String code, String displayName) {
        this.value = EventId.of(code, "http://hl7.org/fhir/audit-event-type", displayName);
    }
}
