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

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ListResource;
import org.ietf.jgss.Oid;

public abstract class FolderList<T extends FolderList<T>> extends MhdList<T> {

    public FolderList() {
        super();
        setCode(new CodeableConcept().addCoding(FOLDER_LIST_CODING));
    }

    /**
     * Adds an identifier to be a EntryUuid as required by the profile
     * @param system system
     * @param value value
     * @return this object
     */
    @SuppressWarnings("unchecked")
    public T setUniqueIdIdentifier(String system, String value) {
        getIdentifier().add(new UniqueIdIdentifier()
            .setSystem(system)
            .setValue(value));
        return (T)this;
    }

    public static final Coding FOLDER_LIST_CODING = new Coding(
        "https://profiles.ihe.net/ITI/MHD/CodeSystem/MHDlistTypes",
        "folder",
        "folder"
    );
}
