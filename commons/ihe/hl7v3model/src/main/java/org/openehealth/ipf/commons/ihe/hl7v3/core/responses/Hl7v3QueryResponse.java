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

import net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01AcknowledgementDetail;

import java.util.List;

/**
 * Response-specific properties of the simplified models of HL7v3 query responses.
 *
 * @author Christian Ohr
 * @since 6.1
 */
public interface Hl7v3QueryResponse extends Hl7v3Response {

    /**
     * @return the query response code, either "OK" (Data Found), "NF" (No Data Found), "QE" (Query Parameter Error)
     * or "AE" (Application Error).
     */
    String getQueryResponseCode();

    void setQueryResponseCode(String queryResponseCode);

    /**
     * @return the acknowledgement details. Shall be provided in case of an error. Never {@code null}.
     */
    List<MCCIMT000300UV01AcknowledgementDetail> getAcknowledgementDetails();

    /**
     * Sets the response code to "Data Found". The patient data will be included in the response.
     */
    default void setDataFound() {
        setAcknowledgementTypeCode("AA");
        setQueryResponseCode("OK");
    }

    /**
     * Sets the response code to "No Data Found". No patient data will be included in the response.
     */
    default void setNoDataFound() {
        setAcknowledgementTypeCode("AA");
        setQueryResponseCode("NF");
    }

    /**
     * Sets the response code to "Query Parameter Error". No patient data will be included in the response.
     */
    default void setQueryParameterError() {
        setAcknowledgementTypeCode("AE");
        setQueryResponseCode("QE");
    }

    /**
     * Sets the response code to "Application Error". No patient data will be included in the response.
     */
    @Override
    default void setApplicationError() {
        setAcknowledgementTypeCode("AE");
        setQueryResponseCode("AE");
    }

    /**
     * Sets the response code to "Application Reject" with query response code "AE". No patient data will be
     * included in the response.
     */
    @Override
    default void setApplicationReject() {
        setAcknowledgementTypeCode("AR");
        setQueryResponseCode("AE");
    }
}
