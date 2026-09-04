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
package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE_PROFILE;

@ResourceDef(name = "DocumentReference", id = "mhdSimplifiedPublishDocumentReference", profile = SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE_PROFILE)
public class SimplifiedPublishDocumentReference extends DocumentReference {

    public SimplifiedPublishDocumentReference() {
        super();
        SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE.setProfile(this);
        setDate(new Date());
        setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
    }

    /**
     * Sets the MasterIdentifier to be a Unique Id as required by the profile
     *
     * @param system system value
     * @param value  identifier value
     * @return this object
     */
    public SimplifiedPublishDocumentReference setUniqueIdIdentifier(String system, String value) {
        setMasterIdentifier(new UniqueIdIdentifier()
            .setSystem(system)
            .setValue(value));
        return this;
    }

    public SimplifiedPublishDocumentReference setContent(String contentType, byte[] content) {
        try {
            addContent().setAttachment(new Attachment()
                .setContentType(contentType)
                // The plain document: FHIR renders it as base64 (CP-ITI-1325-02), and HAPI does
                // that encoding when the resource is serialized
                .setData(content)
                .setSize(content.length)
                .setHash(MessageDigest.getInstance("SHA-1").digest(content)));
            return this;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // should not occur
        }
    }

    @Override
    public SimplifiedPublishDocumentReference copy() {
        var dst = new SimplifiedPublishDocumentReference();
        copyValues(dst);
        return dst;
    }
}
