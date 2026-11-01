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
import net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Reason;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.MatchedPatient;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.XcpdQueryRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Simplified model of an XCPD Cross Gateway Patient Discovery response (ITI-55, PRPA_IN201306UV02).
 * <p>
 * It extends {@link XcpdQueryRequest} for convenience, as the query parameters are echoed in the response.
 * The homeCommunityId of the Responding Gateway is carried in the {@link MatchedPatient#getCustodianOid() custodian
 * OID} of each matched patient.
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class XcpdQueryResponse extends XcpdQueryRequest implements PatientDemographicsQueryResponse {

    private String acknowledgementTypeCode;

    private String queryResponseCode;

    private II targetMessageId;

    private final List<MatchedPatient> patients = new ArrayList<>();

    private final List<MCCIMT000300UV01AcknowledgementDetail> acknowledgementDetails = new ArrayList<>();

    /**
     * Reasons ({@code controlActProcess/reasonOf/detectedIssueEvent}), used by XCPD to signal e.g. that additional
     * demographics are required or that the answer is not available. The list is never {@code null}.
     */
    private final List<MFMIMT700711UV01Reason> reasons = new ArrayList<>();

    /**
     * Empty constructor. By default, it sets the response code to "Application Error".
     */
    public XcpdQueryResponse() {
        super();
        setApplicationError();
    }

    /**
     * Creates and prepopulates a {@link XcpdQueryResponse} from a {@link XcpdQueryRequest}. The receiver and
     * sender are inverted, the query parameters are copied.
     *
     * @param query The query data.
     * @return the created response.
     */
    public static XcpdQueryResponse fromQuery(final XcpdQueryRequest query) {
        Objects.requireNonNull(query);
        final var response = new XcpdQueryResponse();
        response.copyQueryParameters(query);
        response.setSender(query.getReceiver());
        response.setReceiver(query.getSender());
        response.setTargetMessageId(query.getMessageId());
        return response;
    }
}
