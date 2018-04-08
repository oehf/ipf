/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.cxf.audit;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder;

import java.util.ArrayList;
import java.util.List;

import static org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder.PayloadType.SOAP_BODY;


/**
 * A data structure that contains various ATNA audit information pieces
 * common for all Web Service-based IHE transactions.
 * <p>
 * These pieces are stored and used by corresponding CXF interceptors
 * and transaction-specific audit strategies.
 *
 * @author Dmytro Rud
 */
public class WsAuditDataset extends AuditDataset {

    private static final long serialVersionUID = 7940196804508126576L;

    /**
     * Client user ID (WS-Addressing &lt;Reply-To&gt; header).
     */
    @Getter
    @Setter
    private String sourceUserId;

    /**
     * Server user ID (SOAP endpoint)
     */
    @Getter
    @Setter
    private String destinationUserId;

    /**
     * Request SOAP Body (XML) payload.
     */
    @Getter
    private String requestPayload;

    /**
     * IDs and real-world names of human users.
     */
    @Getter
    private final List<HumanUser> humanUsers = new ArrayList<>();

    /**
     * Client IP address.
     */
    @Getter
    @Setter
    private String clientIpAddress;

    /**
     * Purposes of use, see ITI TF-2a section 3.20.7.8 and ITI TF-2b section 3.40.4.1.2.3.
     */
    @Getter
    @Setter
    private PurposeOfUse[] purposesOfUse;

    /**
     * Patient ID from XUA token, see ITI TF-2b Section 3.40.4.1.2.2.1.
     */
    @Getter
    @Setter
    private String xuaPatientId;

    /**
     * Local address
     */
    @Setter
    private String localAddress;

    /**
     * Remote address
     */
    @Setter
    @Getter
    private String remoteAddress;

    @Setter
    private boolean sourceUserIsRequestor = true;

    /**
     * Constructor.
     *
     * @param serverSide specifies whether this audit dataset will be used on the
     *                   server side (<code>true</code>) or on the client side (
     *                   <code>false</code>)
     */
    public WsAuditDataset(boolean serverSide) {
        super(serverSide);
    }

    /**
     * Sets the request SOAP Body (XML) payload.
     *
     * @param requestPayload SOAP Body (XML) payload.
     */
    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    /**
     * Sets the request SOAP Body (XML) payload.
     *
     * @param payloadHolder POJO containing SOAP Body (XML) payload.
     */
    public void setRequestPayload(StringPayloadHolder payloadHolder) {
        this.requestPayload = (payloadHolder != null) ? payloadHolder.get(SOAP_BODY) : null;
    }

    /**
     * @return The machine name or IP address
     */
    public String getLocalAddress() {
        return localAddress != null ? localAddress : AuditUtils.getLocalIPAddress();
    }

    @Override
    public boolean isSourceUserIsRequestor() {
        return sourceUserIsRequestor && super.isSourceUserIsRequestor();
    }
}
