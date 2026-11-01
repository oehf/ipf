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
package org.openehealth.ipf.commons.ihe.hl7v3.core.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.ihe.gazelle.hl7v3.datatypes.II;
import org.openehealth.ipf.commons.ihe.hl7v3.core.Hl7v3Message;

/**
 * Base of the simplified models of HL7v3 queries: message header and query ID.
 * <p>
 * The simplified response models extend their respective query model, because the query parameters are echoed in
 * the response; the response-specific properties are defined by
 * {@link org.openehealth.ipf.commons.ihe.hl7v3.core.responses.Hl7v3QueryResponse}.
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class Hl7v3QueryRequest extends Hl7v3Message {

    /**
     * The query ID. It shall be provided.
     */
    private II queryId;
}
