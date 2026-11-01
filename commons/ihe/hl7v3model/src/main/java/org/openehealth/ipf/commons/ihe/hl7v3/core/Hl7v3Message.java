/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.core;

import lombok.Data;
import net.ihe.gazelle.hl7v3.datatypes.II;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Device;

import java.time.ZonedDateTime;

/**
 * Base of all simplified HL7v3 message models: the message header (transmission wrapper).
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
public abstract class Hl7v3Message {

    /**
     * The message ID. It shall be provided.
     */
    private II messageId;

    /**
     * The sender device. It shall be provided.
     */
    private Device sender;

    /**
     * The receiver device. It shall be provided.
     */
    private Device receiver;

    /**
     * The message creation time. Initialized with the current time.
     */
    private ZonedDateTime creationTime;

    protected Hl7v3Message() {
        this.creationTime = ZonedDateTime.now();
    }
}
