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
package org.openehealth.ipf.commons.ihe.xds.core.cxf.audit;

import java.util.List;

import org.openehealth.ipf.commons.ihe.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;

/**
 * A data structure used to store information pieces collected by various
 * auditing-related CXF interceptors.
 * 
 * @author Dmytro Rud
 */
public class ItiAuditDataset extends AuditDataset {

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
    // patient ID as HL7 CX datatype, e.g. "1234^^^&1.2.3.4&ISO"
    private String patientId;
    // submission set unique ID
    private String submissionSetUuid;

    /**
     * Constructor.
     * 
     * @param serverSide
     *            specifies whether this audit dataset will be used on the
     *            server side (<code>true</code>) or on the client side (
     *            <code>false</code>)
     */
    public ItiAuditDataset(boolean serverSide) {
        super(serverSide);
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setServiceEndpointUrl(String serviceEntpointUrl) {
        this.serviceEndpointUrl = serviceEntpointUrl;
    }

    public String getServiceEndpointUrl() {
        return serviceEndpointUrl;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setSubmissionSetUniqueID(String submissionSetUuid) {
        this.submissionSetUuid = submissionSetUuid;
    }

    public String getSubmissionSetUuid() {
        return submissionSetUuid;
    }

    /**
     * Enriches the set with fields extracted from a submit objects request POJO.
     * 
     * @param request
     *      a {@link EbXMLSubmitObjectsRequest} as POJO 
     */
    public void enrichDatasetFromSubmitObjectsRequest(EbXMLSubmitObjectsRequest ebXML) 
    {
        List<EbXMLRegistryPackage> submissionSets = 
            ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE);
        
        for (EbXMLRegistryPackage submissionSet : submissionSets) {
            String patientID = submissionSet.getExternalIdentifierValue(
                    Vocabulary.SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID);            
            setPatientId(patientID);
            
            String uniqueID = submissionSet.getExternalIdentifierValue(
                    Vocabulary.SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID);
            setSubmissionSetUniqueID(uniqueID);
        }
    }
}
