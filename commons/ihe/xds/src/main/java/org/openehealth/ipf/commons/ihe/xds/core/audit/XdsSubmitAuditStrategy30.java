/*
 * Copyright 2012 the original author or authors.
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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;

import java.util.Map;

/**
 * Basis for Strategy pattern implementation for ATNA Auditing
 * in ebXML 3.0-based submission-related XDS transactions.
 *
 * @author Dmytro Rud
 */
public abstract class XdsSubmitAuditStrategy30 extends XdsAuditStrategy<XdsSubmitAuditDataset> {

    /**
     * Constructs an audit strategy for an XDS submission.
     *
     * @param serverSide
     *      whether this is a server-side or a client-side strategy.
     */
    public XdsSubmitAuditStrategy30(boolean serverSide) {
        super(serverSide);
    }

    protected static void enrichDatasetFromSubmitObjectsRequest(XdsSubmitAuditDataset auditDataset, EbXMLSubmitObjectsRequest ebXML) {
        var submissionSets = ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE);
        auditDataset.setHomeCommunityId(ebXML.getSingleSlotValue(Vocabulary.SLOT_NAME_HOME_COMMUNITY_ID));

        submissionSets.forEach(submissionSet -> {
            var patientId = submissionSet.getExternalIdentifierValue(Vocabulary.SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID);
            auditDataset.getPatientIds().add(patientId);
            var uniqueId = submissionSet.getExternalIdentifierValue(Vocabulary.SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID);
            auditDataset.setSubmissionSetUuid(uniqueId);
            if(auditDataset.getHomeCommunityId() == null) {
                auditDataset.setHomeCommunityId(submissionSet.getHome());
            }
        });
    }

    @Override
    public XdsSubmitAuditDataset enrichAuditDatasetFromRequest(XdsSubmitAuditDataset auditDataset, Object pojo, Map<String, Object> parameters) {
        if (pojo instanceof SubmitObjectsRequest) {
            var submitObjectsRequest = (SubmitObjectsRequest) pojo;
            EbXMLSubmitObjectsRequest ebXML = new EbXMLSubmitObjectsRequest30(submitObjectsRequest);
            enrichDatasetFromSubmitObjectsRequest(auditDataset, ebXML);
        }
        return auditDataset;
    }



    @Override
    public XdsSubmitAuditDataset createAuditDataset() {
        return new XdsSubmitAuditDataset(isServerSide());
    }
}
