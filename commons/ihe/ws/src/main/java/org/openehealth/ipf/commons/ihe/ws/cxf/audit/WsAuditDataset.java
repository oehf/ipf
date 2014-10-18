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
import org.apache.cxf.wsdl.EndpointReferenceUtils;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

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
     * Event outcome code as defined in RFC 3881.
     */
    @Getter @Setter private RFC3881EventOutcomeCodes eventOutcomeCode;

    /**
     * Request SOAP Body (XML) payload.
     */
    @Getter private String requestPayload;

    /**
     * Client user ID (WS-Addressing &lt;Reply-To&gt; header).
     */
    @Setter private String userId;

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
    @Getter private final List<CodedValueType> purposesOfUse = new ArrayList<CodedValueType>();


    /**
     * Constructor.
     * 
     * @param serverSide
     *            specifies whether this audit dataset will be used on the
     *            server side (<code>true</code>) or on the client side (
     *            <code>false</code>)
     */
    public WsAuditDataset(boolean serverSide) {
        super(serverSide);
    }

    /**
     * Sets the request SOAP Body (XML) payload.
     * @param requestPayload
     *          SOAP Body (XML) payload.
     */
    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    /**
     * Sets the request SOAP Body (XML) payload.
     * @param payloadHolder
     *          POJO containing SOAP Body (XML) payload.
     */
    public void setRequestPayload(StringPayloadHolder payloadHolder) {
        this.requestPayload = (payloadHolder != null) ? payloadHolder.get(SOAP_BODY) : null;
    }

    /**
     * Returns User ID.
     * <p>
     * When the user ID could not be extracted from WS-Addressing header
     * &lt;ReplyTo&gt;, the special "WS-Addressing anonymous address" 
     * will be returned, as prescribed in 
     * http://www.w3.org/TR/2006/REC-ws-addr-soap-20060509/#anonaddress
     * @return client user ID (WS-Addressing &lt;Reply-To&gt; header).
     */
    public String getUserId() {
        return (userId != null) ? userId : EndpointReferenceUtils.ANONYMOUS_ADDRESS;
    }

}
