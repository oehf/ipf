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
package org.openehealth.ipf.commons.ihe.hl7v3.core.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01AcknowledgementDetail;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.MatchedPatient;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PdqV3QueryRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Simplified model of a PDQv3 query response (ITI-47, PRPA_IN201306UV02).
 * <p>
 * It extends {@link PdqV3QueryRequest} for convenience, as the query parameters are echoed in the response.
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PdqV3QueryResponse extends PdqV3QueryRequest implements PatientDemographicsQueryResponse {

    private String acknowledgementTypeCode;

    private String queryResponseCode;

    private II targetMessageId;

    private final List<MatchedPatient> patients = new ArrayList<>();

    private final List<MCCIMT000300UV01AcknowledgementDetail> acknowledgementDetails = new ArrayList<>();

    /**
     * Total number of matches (Continuation option, {@code queryAck/resultTotalQuantity}).
     */
    private Integer resultTotalQuantity;

    /**
     * Number of matches in this response (Continuation option, {@code queryAck/resultCurrentQuantity}).
     */
    private Integer resultCurrentQuantity;

    /**
     * Number of matches remaining to be fetched (Continuation option, {@code queryAck/resultRemainingQuantity}).
     */
    private Integer resultRemainingQuantity;

    /**
     * Empty constructor. By default, it sets the response code to "Application Error".
     */
    public PdqV3QueryResponse() {
        super();
        setApplicationError();
    }

    /**
     * Creates and prepopulates a {@link PdqV3QueryResponse} from a {@link PdqV3QueryRequest}. The receiver and
     * sender are inverted, the query parameters are copied.
     *
     * @param query The query data.
     * @return the created response.
     */
    public static PdqV3QueryResponse fromQuery(final PdqV3QueryRequest query) {
        Objects.requireNonNull(query);
        final var response = new PdqV3QueryResponse();
        response.copyQueryParameters(query);
        response.setSender(query.getReceiver());
        response.setReceiver(query.getSender());
        response.setTargetMessageId(query.getMessageId());
        return response;
    }
}
