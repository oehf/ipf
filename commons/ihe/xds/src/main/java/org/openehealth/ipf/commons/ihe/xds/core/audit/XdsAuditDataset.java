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
package org.openehealth.ipf.commons.ihe.xds.core.audit;

import java.util.List;

import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;

/**
 * A data structure that contains XDS-specific ATNA audit information pieces
 * in addition to common IHE Web Service-related ones.
 * @author Dmytro Rud
 */
public class XdsAuditDataset extends WsAuditDataset {
    private static final long serialVersionUID = 652866992858926778L;

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
    public XdsAuditDataset(boolean serverSide) {
        super(serverSide);
    }

    /**
     * Sets the patient ID as HL7 CX datatype, e.g. "1234^^^&1.2.3.4&ISO"
     * @param patientId
     *          patient ID as HL7 CX datatype, e.g. "1234^^^&1.2.3.4&ISO".
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return patient ID as HL7 CX datatype, e.g. "1234^^^&1.2.3.4&ISO".
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the submission set unique ID.
     * @param submissionSetUuid
     *          submission set unique ID.
     */
    public void setSubmissionSetUniqueID(String submissionSetUuid) {
        this.submissionSetUuid = submissionSetUuid;
    }

    /**
     * @return submission set unique ID.
     */
    public String getSubmissionSetUuid() {
        return submissionSetUuid;
    }

    /**
     * Enriches the set with fields extracted from a submit objects request POJO.
     * 
     * @param ebXML
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
