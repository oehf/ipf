/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.audit;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;

import java.util.List;

abstract public class XdsSubmitAuditStrategy extends XdsAuditStrategy<XdsSubmitAuditDataset> {

    public XdsSubmitAuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    protected static void enrichDatasetFromSubmitObjectsRequest(XdsSubmitAuditDataset auditDataset, EbXMLSubmitObjectsRequest ebXML) {
        List<EbXMLRegistryPackage> submissionSets = ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE);
        auditDataset.setHomeCommunityId(ebXML.getSingleSlotValue(Vocabulary.SLOT_NAME_HOME_COMMUNITY_ID));

        for (EbXMLRegistryPackage submissionSet : submissionSets) {
            String patientId = submissionSet.getExternalIdentifierValue(Vocabulary.SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID);
            auditDataset.getPatientIds().add(patientId);

            String uniqueId = submissionSet.getExternalIdentifierValue(Vocabulary.SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID);
            auditDataset.setSubmissionSetUuid(uniqueId);
        }
    }

    @Override
    public XdsSubmitAuditDataset createAuditDataset() {
        return new XdsSubmitAuditDataset(isServerSide());
    }

}
