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
import org.openehealth.ipf.commons.ihe.atna.AuditDataset;

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

    // SOAP Body (XML) payload
    private String payload;
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
     * Sets the SOAP Body (XML) payload.
     * @param payload
     *          SOAP Body (XML) payload.
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    /**
     * @return SOAP Body (XML) payload.
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Sets the client user ID (WS-Addressing <Reply-To> header).
     * @param userId
     *          client user ID (WS-Addressing <Reply-To> header).
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
     * @return client user ID (WS-Addressing <Reply-To> header).
     */
    public String getUserId() {
        return (userId != null) ? userId : EndpointReferenceUtils.ANONYMOUS_ADDRESS;
    }

    /**
     * Sets the client user name (WS-Security <Username> header).
     * @param userName
     *          client user name (WS-Security <Username> header).
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return client user name (WS-Security <Username> header).
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

}
