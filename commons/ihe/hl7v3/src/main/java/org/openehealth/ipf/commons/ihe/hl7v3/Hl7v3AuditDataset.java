/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3;

import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;


/**
 * Generic audit dataset for IHE PIX/PDQ v3 transactions.
 * @author Dmytro Rud
 */
public class Hl7v3AuditDataset extends WsAuditDataset {
    private static final long serialVersionUID = -7303748425104562452L;

    /** HL7v3 message ID. */
    private String messageId;

    /** HL7v3 query ID. */
    private String queryId;

    /** Home ID of the target community. */
    private String homeCommunityId;

    /** Request message type (name of the root element). */
    private String requestType;

    /** Patient ID list. */
    private String[] patientIds;

    /** Old patient ID. */
    private String oldPatientId;


    /**
     * Constructor.
     * @param serverSide
     *      Where we are&nbsp;&mdash; server side
     *      (<code>true</code>) or client side (<code>false</code>).
     */
    public Hl7v3AuditDataset(boolean serverSide) {
        super(serverSide);
    }


    // ============= automatically generated getters and setters =============

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String[] getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(String[] patientIds) {
        this.patientIds = patientIds;
    }

    public String getPatientId() {
        return ((patientIds != null) && (patientIds.length > 0)) ? patientIds[0] : null;
    }

    public String getOldPatientId() {
        return oldPatientId;
    }

    public void setOldPatientId(String oldPatientId) {
        this.oldPatientId = oldPatientId;
    }
}
