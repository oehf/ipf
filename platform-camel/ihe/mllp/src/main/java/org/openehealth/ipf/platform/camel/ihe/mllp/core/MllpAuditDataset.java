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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;


/**
 * Generic audit dataset for IHE PIX/PDQ transactions.
 * 
 * @author Dmytro Rud
 */
public class MllpAuditDataset extends AuditDataset {
    private static final long serialVersionUID = -4427222097816361541L;

    /** Sending application from MSH-3 */
    private String sendingApplication;
    
    /** Sending facility from MSH-4 */
    private String sendingFacility;

    /** Receiving application from MSH-5 */
    private String receivingApplication;

    /** Receiving facility from MSH-6 */
    private String receivingFacility;
    
    /** Message type from MSH-9 */
    private String messageType;
    
    /** Message control ID from MSH-10 */
    private String messageControlId;

    /** Local address from MINA session */
    private String localAddress;

    /** Remote address from MINA session */
    private String remoteAddress;

    /** Patient ID list from PID-3. */
    private String patientId;

    /** Old patient ID list from MRG-1 (for A40 only). */
    private String oldPatientId;

    /** Patient ID list from PID-3. */
    private String[] patientIds;

    /** Request payload. */
    private String payload;


    
    public static final String[] GENERIC_NECESSARY_AUDIT_FIELDS = new String[] {
        "SendingApplication",
        "SendingFacility",
        "ReceivingApplication",
        "ReceivingFacility",
        "MessageControlId",
        "LocalAddress",
        "RemoteAddress"
    };
    
    
    /**
     * Constructor.
     * @param serverSide
     *      Where we are&nbsp;&mdash; server side
     *      ({@code true}) or client side ({@code false}).
     */
    public MllpAuditDataset(boolean serverSide) {
        super(serverSide);
    }

    
    // ----- automatically generated getters and setters -----
    
    public String getSendingFacility() {
        return sendingFacility;
    }

    public void setSendingFacility(String sendingFacility) {
        this.sendingFacility = sendingFacility;
    }

    public String getSendingApplication() {
        return sendingApplication;
    }

    public void setSendingApplication(String sendingApplication) {
        this.sendingApplication = sendingApplication;
    }

    public String getReceivingFacility() {
        return receivingFacility;
    }

    public void setReceivingFacility(String receivingFacility) {
        this.receivingFacility = receivingFacility;
    }

    public String getReceivingApplication() {
        return receivingApplication;
    }

    public void setReceivingApplication(String receivingApplication) {
        this.receivingApplication = receivingApplication;
    }

    public String getMessageControlId() {
        return messageControlId;
    }

    public void setMessageControlId(String messageControlId) {
        this.messageControlId = messageControlId;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setOldPatientId(String oldPatientId) {
        this.oldPatientId = oldPatientId;
    }

    public String getOldPatientId() {
        return oldPatientId;
    }

    public void setPatientIds(String[] patientIds) {
        this.patientIds = patientIds;
    }

    public String[] getPatientIds() {
        return patientIds;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

}
