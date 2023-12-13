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
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Identifier;
import org.openehealth.ipf.commons.core.URN;
import org.openehealth.ipf.commons.ihe.fhir.Constants;

import java.util.Date;
import java.util.UUID;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfiles.MINIMAL_DOCUMENT_REFERENCE;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfiles.MINIMAL_DOCUMENT_REFERENCE_PROFILE;

@ResourceDef(name = "DocumentReference", id = "mhdMinimalDocumentReference", profile = MINIMAL_DOCUMENT_REFERENCE_PROFILE)
public class MinimalDocumentReference<T extends MinimalDocumentReference<T>> extends DocumentReference {

    public MinimalDocumentReference() {
        super();
        MINIMAL_DOCUMENT_REFERENCE.setProfile(this);
        setDate(new Date());
        setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
    }

    /**
     * Sets the MasterIdentifier to be a Unique Id as required by the profile
     *
     * @param system system value
     * @param value identifier value
     * @return this object
     */
    @SuppressWarnings("unchecked")
    public T setUniqueIdIdentifier(String system, String value) {
        setMasterIdentifier(new Identifier()
            .setUse(Identifier.IdentifierUse.USUAL)
            .setSystem(system)
            .setValue(value));
        return (T)this;
    }

    /**
     * Adds an identifier to be a EntryUuid as required by the profile
     * @param uuid UUID
     * @return this object
     */
    @SuppressWarnings("unchecked")
    public T setEntryUuidIdentifier(UUID uuid) {
        getIdentifier().add(new Identifier()
            .setUse(Identifier.IdentifierUse.OFFICIAL)
            .setSystem(Constants.URN_IETF_RFC_3986)
            .setValue(new URN(uuid).toString()));
        return (T)this;
    }
}
