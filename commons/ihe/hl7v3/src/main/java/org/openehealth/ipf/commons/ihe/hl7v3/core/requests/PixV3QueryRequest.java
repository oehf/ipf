/*
 * Copyright 2021 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.core.requests;

import lombok.Data;
import net.ihe.gazelle.hl7v3.datatypes.II;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Device;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Simplified model of a PIXV3 Query query (ITI-45).
 *
 * @author Quentin Ligier
 * @since 4.1
 */
@Data
public class PixV3QueryRequest {

    /**
     * The patient identifier known to the PIX Consumer. It shall be provided.
     */
    private II queryPatientId;

    /**
     * The identifiers for the Patient Identity Domain's assigning authority. It may be empty but is never {@code null}.
     */
    private final Set<String> dataSourceOids = new HashSet<>();

    /**
     * Unique identifier for the message. It shall be provided.
     */
    private II messageId;

    /**
     * Unique identifier for the query. It shall be provided.
     */
    private II queryId;

    /**
     * The Patient Registry Query placer. It shall be provided.
     */
    private Device sender;

    /**
     * The Patient Registry Query fulfiller. It shall be provided.
     */
    private Device receiver;

    /**
     * The query creation time. It shall be provided.
     */
    private ZonedDateTime creationTime;

    /**
     * Empty constructor.
     */
    public PixV3QueryRequest() {
        this.setCreationTime(ZonedDateTime.now());
    }
}
