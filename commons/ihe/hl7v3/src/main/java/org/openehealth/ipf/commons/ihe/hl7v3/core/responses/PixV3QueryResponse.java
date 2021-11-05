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
package org.openehealth.ipf.commons.ihe.hl7v3.core.responses;

import lombok.*;
import net.ihe.gazelle.hl7v3.coctmt150003UV03.COCTMT150003UV03Organization;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.PN;
import net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01AcknowledgementDetail;
import net.ihe.gazelle.hl7v3.prpamt201304UV02.PRPAMT201304UV02OtherIDs;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PixV3QueryRequest;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Simplified model of a PIXV3 Query response (ITI-45).
 *
 * <p>It extends {@link PixV3QueryRequest} for convenience, as most properties are duplicated between both models.
 *
 * @author Quentin Ligier
 * @since 4.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PixV3QueryResponse extends PixV3QueryRequest {

    /**
     * The acknowledgement type code, either "AA" (Application Accept) or "AE" (Application Error). It shall be
     * provided. Please use the methods {@link #setDataFound()}, {@link #setNoDataFound()} and
     * {@link #setApplicationError()} instead of the setter.
     */
    private String acknowledgementTypeCode;

    /**
     * The query response code, either "OK" (Data Found), "NF" (No Data Found) or "AE" (Application Error). It shall be
     * provided. Please use the methods {@link #setDataFound()}, {@link #setNoDataFound()} and
     * {@link #setApplicationError()} instead of the setter.
     */
    private String queryResponseCode;

    /**
     * The targeted message ID. It shall be provided.
     */
    private II targetMessageId;

    /**
     * The list of patient ID values (OID). At least one ID shall be provided if data is found, the list is
     * never {@code null}.
     */
    private final List<II> patientIds = new ArrayList<>();

    /**
     * The list of person names. Exactly one name shall be provided if data is found.
     */
    private PN personName;

    /**
     * The list of IDs associated with the person, not the patient. It may be empty but is never {@code null}.
     */
    private final List<II> personIds = new ArrayList<>();

    /**
     * The list of other patient IDs. It may be empty but is never {@code null}.
     */
    private final List<PRPAMT201304UV02OtherIDs> asOtherIDs = new ArrayList<>();

    /**
     * The details of the provider organization. It shall be provided.
     */
    private COCTMT150003UV03Organization providerOrganization;

    /**
     * The custodian OID. It shall be provided.
     */
    private String custodianOid;

    /**
     * The list of acknowledgement details in the message. It shall be provided if the status is "AE", the list may be
     * empty but is never {@code null}.
     */
    private final List<MCCIMT000300UV01AcknowledgementDetail> acknowledgementDetails = new ArrayList<>();

    /**
     * Empty constructor. By default, it sets the response code to "Application Error".
     */
    public PixV3QueryResponse() {
        this.setCreationTime(ZonedDateTime.now());
        this.setApplicationError();
    }

    /**
     * Creates and prepopulate a {@link PixV3QueryResponse} from a {@link PixV3QueryRequest}. The receiver and sender
     * are inverted.
     *
     * @param query The query data.
     * @return the created response.
     */
    public static PixV3QueryResponse fromQuery(final PixV3QueryRequest query) {
        Objects.requireNonNull(query);
        final var response = new PixV3QueryResponse();
        response.setQueryPatientId(query.getQueryPatientId());
        response.getDataSourceOids().addAll(query.getDataSourceOids());
        response.setSender(query.getReceiver()); // Receiver and sender are inverted in the response
        response.setReceiver(query.getSender());
        response.setTargetMessageId(query.getMessageId());
        response.setQueryId(query.getQueryId());
        return response;
    }

    /**
     * Sets the response code to "Data Found". The patient data will be included in the response.
     */
    public void setDataFound() {
        this.acknowledgementTypeCode = "AA";
        this.queryResponseCode = "OK";
    }

    /**
     * Sets the response code to "No Data Found". No patient data will be included in the response.
     */
    public void setNoDataFound() {
        this.acknowledgementTypeCode = "AA";
        this.queryResponseCode = "NF";
    }

    /**
     * Sets the response code to "Application Error". No patient data will be included in the response.
     */
    public void setApplicationError() {
        this.acknowledgementTypeCode = "AE";
        this.queryResponseCode = "AE";
    }
}
