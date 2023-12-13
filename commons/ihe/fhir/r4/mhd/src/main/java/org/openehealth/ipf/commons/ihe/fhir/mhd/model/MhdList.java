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

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.ListResource;
import org.ietf.jgss.Oid;
import org.openehealth.ipf.commons.core.URN;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.mhd.Mhd421;

import java.util.Date;
import java.util.UUID;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.MHD_LIST_PROFILE;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.MHD_LIST;

@ResourceDef(name = "List", id = "mhdList", profile = MHD_LIST_PROFILE)
public class MhdList<T extends MhdList<T>> extends ListResource implements Mhd421 {

    public MhdList() {
        super();
        setDate(new Date());
        setStatus(ListStatus.CURRENT);
        setMode(ListMode.WORKING);
        MHD_LIST.setProfile(this);
    }

    /**
     * Defines what this List is for (system = <a href="https://profiles.ihe.net/ITI/MHD/CodeSystem/MHDlistTypes">...</a>)
     * <ul>
     * <li>folder : Folder as a FHIR List</li>
     * <li>submissionset : SubmissionSet as a FHIR List</li>
     * </ul>
     */
    @Child(name = "designationType")
    @Extension(url = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/ihe-designationType", definedLocally = false)
    @Description(shortDefinition = "Clinical code of the List")
    private CodeableConcept designationType;

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(designationType);
    }

    public CodeableConcept getDesignationType() {
        if (designationType == null) {
            designationType = new CodeableConcept();
        }
        return designationType;
    }

    public T setDesignationType(CodeableConcept designationType) {
        this.designationType = designationType;
        return (T)this;
    }

    public boolean hasDesignationType() {
        return this.designationType != null && !this.designationType.isEmpty();
    }


    /**
     * Adds an identifier to be a EntryUuid as required by the profile
     * @param uuid uuid
     * @return this object
     */
    @SuppressWarnings("unchecked")
    public T setEntryUuidIdentifier(UUID uuid) {
        getIdentifier().add(new EntryUuidIdentifier(uuid));
        return (T)this;
    }

}
