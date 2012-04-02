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

import org.apache.cxf.wsdl.EndpointReferenceUtils;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

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

    // event outcome code as defined in RFC 3881
    private RFC3881EventOutcomeCodes eventOutcomeCode;
    // request SOAP Body (XML) payload
    private String requestPayload;
    // client user ID (WS-Addressing <Reply-To> header)
    private String userId;
    // client user name (WS-Security <Username> header)
    private String userName;
    // client IP address
    private String clientIpAddress;
    // service (i.e. registry or repository) endpoint URL
    private String serviceEndpointUrl;

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
     * @return request SOAP Body (XML) payload.
     */
    public String getRequestPayload() {
        return requestPayload;
    }

    /**
     * Sets the client user ID (WS-Addressing &gt;Reply-To&lt; header).
     * @param userId
     *          client user ID (WS-Addressing &gt;Reply-To&lt; header).
     */
    public void setUserId(String userId) {
        this.userId = userId;
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

    /**
     * Sets the client user name (WS-Security &lt;Username&gt; header).
     * @param userName
     *          client user name (WS-Security &lt;Username&gt; header).
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return client user name (WS-Security &lt;Username&gt; header).
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the client IP address
     * @param clientIpAddress
     *          client IP address.
     */
    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    /**
     * @return client IP address.
     */
    public String getClientIpAddress() {
        return clientIpAddress;
    }

    /**
     * Sets the service (i.e. registry or repository) endpoint URL.
     * @param serviceEntpointUrl
     *          service (i.e. registry or repository) endpoint URL.
     */
    public void setServiceEndpointUrl(String serviceEntpointUrl) {
        serviceEndpointUrl = serviceEntpointUrl;
    }

    /**
     * @return service (i.e. registry or repository) endpoint URL.
     */
    public String getServiceEndpointUrl() {
        return serviceEndpointUrl;
    }

    /**
     * @return RFC 3881 event outcome code.
     */
    public RFC3881EventOutcomeCodes getEventOutcomeCode() {
        return eventOutcomeCode;
    }

    /**
     * Sets the RFC 3881 event outcome code.
     * @param eventOutcomeCode
     *      RFC 3881 event outcome code.
     */
    public void setEventOutcomeCode(RFC3881EventOutcomeCodes eventOutcomeCode) {
        this.eventOutcomeCode = eventOutcomeCode;
    }

}
