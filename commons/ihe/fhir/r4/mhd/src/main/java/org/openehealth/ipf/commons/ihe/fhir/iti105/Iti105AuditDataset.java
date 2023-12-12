/*
 * Copyright 2023 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti105;

import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.r4.model.DocumentReference;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;

/**
 * @author Boris Stanojevic
 * @since 4.8
 */
public class Iti105AuditDataset extends FhirAuditDataset {

    // Document reference unique ID
    @Getter @Setter
    private String documentReferenceId;

    public Iti105AuditDataset(boolean serverSide) {
        super(serverSide);
    }


    public void enrichDatasetFromDocumentReference(DocumentReference documentReference) {
        var reference = documentReference.getSubject();
        getPatientIds().add(reference.getResource() != null ?
                reference.getResource().getIdElement().getValue() :
                reference.getReference());
        // If available, use the documentReference masterIdentifier as documentReferenceId
        if (documentReference.hasMasterIdentifier() && documentReference.getMasterIdentifier().hasValue()) {
            this.documentReferenceId = documentReference.getMasterIdentifier().getValue();
        }
    }
}
