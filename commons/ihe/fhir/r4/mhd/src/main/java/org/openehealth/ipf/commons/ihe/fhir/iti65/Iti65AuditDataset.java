/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.iti65;

import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.r4.model.DocumentManifest;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.SubmissionSetList;

/**
 * @author Christian Ohr
 * @since 3.6
 */
public class Iti65AuditDataset extends FhirAuditDataset {

    // Submission Set unique ID
    @Getter @Setter
    private String submissionSetUuid;

    public Iti65AuditDataset(boolean serverSide) {
        super(serverSide);
    }


    public void enrichDatasetFromDocumentManifest(DocumentManifest documentManifest) {
        var reference = documentManifest.getSubject();
        getPatientIds().add(reference.getResource() != null ?
                reference.getResource().getIdElement().getValue() :
                reference.getReference());
        // If available, use the documentManifest identifier as submissionSetUuid
        if (!documentManifest.getIdentifier().isEmpty()) {
            this.submissionSetUuid = documentManifest.getIdentifier().get(0).getValue();
        }
    }

    public void enrichDatasetFromSubmissionSetList(SubmissionSetList<?> submissionSetList) {
        var reference = submissionSetList.getSubject();
        getPatientIds().add(reference.getResource() != null ?
            reference.getResource().getIdElement().getValue() :
            reference.getReference());
        // If available, use the submissionSetList identifier as submissionSetUuid
        if (!submissionSetList.getIdentifier().isEmpty()) {
            this.submissionSetUuid = submissionSetList.getIdentifier().get(0).getValue();
        }
    }
}
