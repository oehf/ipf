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

import net.ihe.gazelle.hl7v3.datatypes.II;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PdqV3QueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PixV3QueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.XcpdQueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.AcceptAcknowledgement;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PdqV3QueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PixV3QueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.XcpdQueryResponse;

import java.util.UUID;

/**
 * Utilities for creating simplified HL7v3 response models from request models, comparable to
 * {@code MessageUtils} for HL7v2.
 * <p>
 * The created responses have a new random message ID and the current creation time, refer to the request's message
 * ID, and have sender and receiver inverted. Query responses additionally echo the query ID and query parameters.
 *
 * @author Christian Ohr
 * @since 6.1
 */
public final class Hl7v3MessageUtils {

    private Hl7v3MessageUtils() {
    }

    /**
     * Creates a response to a PIXv3 query (ITI-45). The response is initialized as "Application Error";
     * use {@link PixV3QueryResponse#setDataFound()} or {@link PixV3QueryResponse#setNoDataFound()} to change it.
     *
     * @param request the query
     * @return the response
     */
    public static PixV3QueryResponse respond(final PixV3QueryRequest request) {
        final var response = PixV3QueryResponse.fromQuery(request);
        response.setMessageId(newMessageId());
        return response;
    }

    /**
     * Creates a response to a PDQv3 query (ITI-47). The response is initialized as "Application Error";
     * use {@link PdqV3QueryResponse#setDataFound()} or {@link PdqV3QueryResponse#setNoDataFound()} to change it.
     *
     * @param request the query
     * @return the response
     */
    public static PdqV3QueryResponse respond(final PdqV3QueryRequest request) {
        final var response = PdqV3QueryResponse.fromQuery(request);
        response.setMessageId(newMessageId());
        return response;
    }

    /**
     * Creates a response to an XCPD query (ITI-55). The response is initialized as "Application Error";
     * use {@link XcpdQueryResponse#setDataFound()} or {@link XcpdQueryResponse#setNoDataFound()} to change it.
     *
     * @param request the query
     * @return the response
     */
    public static XcpdQueryResponse respond(final XcpdQueryRequest request) {
        final var response = XcpdQueryResponse.fromQuery(request);
        response.setMessageId(newMessageId());
        return response;
    }

    /**
     * Creates a positive ("Application Accept") acknowledgement of a patient identity feed (ITI-44) or
     * PIX update notification (ITI-46) message. Use {@link AcceptAcknowledgement#setApplicationError()} and add
     * {@link AcceptAcknowledgement#getAcknowledgementDetails() acknowledgement details} to signal an error instead.
     *
     * @param request the acknowledged message
     * @return the acknowledgement
     */
    public static AcceptAcknowledgement ack(final PatientRecordRequest request) {
        final var acknowledgement = AcceptAcknowledgement.fromRequest(request);
        acknowledgement.setMessageId(newMessageId());
        acknowledgement.setApplicationAccept();
        return acknowledgement;
    }

    /**
     * @return a new unique message ID with a random UUID as root.
     */
    public static II newMessageId() {
        return new II(UUID.randomUUID().toString(), null);
    }
}
