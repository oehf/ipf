/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

import java.util.ArrayList;
import java.util.List;


/**
 * Generic audit dataset for MLLP-based IHE transactions.
 * 
 * @author Dmytro Rud
 */
abstract public class FhirAuditDataset extends AuditDataset {
    private static final long serialVersionUID = -4427222097816361541L;

    /**
     * Event outcome code as defined in RFC 3881.
     */
    @Getter @Setter private RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcomeCode;

    /**
     * Request SOAP Body (XML) payload.
     */
    @Getter private String requestPayload;

    /**
     * Client user ID (WS-Addressing &lt;Reply-To&gt; header).
     */
    @Getter @Setter private String userId;

    /**
     * Client user name (WS-Security &lt;Username&gt; header).
     */
    @Getter @Setter private String userName;

    /**
     * Client IP address.
     */
    @Getter @Setter private String clientIpAddress;

    /**
     * Service (i.e. registry or repository) endpoint URL.
     */
    @Getter @Setter private String serviceEndpointUrl;

    /**
     * Purposes of use, see ITI TF-2a section 3.20.7.8 and ITI TF-2b section 3.40.4.1.2.3.
     */
    @Getter private final List<CodedValueType> purposesOfUse = new ArrayList<>();

    @Getter @Setter private String[] patientIds;

    /**
     * Constructor.
     * @param serverSide
     *      Where we are&nbsp;&mdash; server side
     *      ({@code true}) or client side ({@code false}).
     */
    public FhirAuditDataset(boolean serverSide) {
        super(serverSide);
    }

}
