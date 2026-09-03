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
import org.hl7.fhir.r4.model.Identifier;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.MhdIdentifierType;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.SubmissionSetList;

import java.util.List;
import java.util.Optional;

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
        // If available, use the documentManifest entryUUID identifier as submissionSetUuid
        entryUuid(documentManifest.getIdentifier()).ifPresent(value -> this.submissionSetUuid = value);
    }

    public void enrichDatasetFromSubmissionSetList(SubmissionSetList<?> submissionSetList) {
        var reference = submissionSetList.getSubject();
        getPatientIds().add(reference.getResource() != null ?
            reference.getResource().getIdElement().getValue() :
            reference.getReference());
        // If available, use the submissionSetList entryUUID identifier as submissionSetUuid
        entryUuid(submissionSetList.getIdentifier()).ifPresent(value -> this.submissionSetUuid = value);
    }

    /**
     * Picks the entryUUID identifier out of the identifiers of a SubmissionSet. Since MHD 4.2.4 the
     * SubmissionSet may carry a uniqueId identifier next to the entryUUID one, so the entryUUID has
     * to be selected by its {@code type} coding (or, for MHD 4.2.3 and earlier, by its {@code use})
     * rather than by its position. Falls back to the first identifier for senders that provide
     * neither.
     *
     * @param identifiers identifiers of the SubmissionSet
     * @return value of the entryUUID identifier, if any identifier is present at all
     */
    private static Optional<String> entryUuid(List<Identifier> identifiers) {
        return MhdIdentifierType.ENTRY_UUID.find(identifiers)
            .or(() -> identifiers.stream().findFirst())
            .map(Identifier::getValue);
    }
}
