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

package org.openehealth.ipf.commons.audit.event;

import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.audit.model.Validateable;

import static java.util.Objects.requireNonNull;

/**
 * Base interface for building DICOM audit messages
 *
 * @author Christian Ohr
 * @since 3.5
 */
public interface AuditMessageBuilder<T extends AuditMessageBuilder<T>> extends Validateable {

    AuditMessage getMessage();

    default AuditMessage[] getMessages() {
        return new AuditMessage[]{getMessage()};
    }


    /**
     * Create and set a Type Value Pair instance for a given type and value
     *
     * @param type  The type to set
     * @param value The value to set
     * @return The Type Value Pair instance
     */
    default TypeValuePairType getTypeValuePair(String type, Object value) {
        return new TypeValuePairType(
                requireNonNull(type, "Type of TypeValuePair must not be null"),
                requireNonNull(value, "Value of TypeValuePair must not be null").toString());
    }
}
