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
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.ListResource;
import org.openehealth.ipf.commons.ihe.fhir.mhd.Mhd421;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.MHD_LIST;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.MHD_LIST_PROFILE;

@SuppressWarnings("unchecked")
@ResourceDef(name = "List", id = "mhdList", profile = MHD_LIST_PROFILE)
public class MhdList<T extends MhdList<T>> extends ListResource implements Mhd421 {

    public MhdList() {
        super();
        setDate(new Date());
        setStatus(ListStatus.CURRENT);
        setMode(ListMode.WORKING);
        MHD_LIST.setProfile(this);
    }

    @Child(name = "designationType")
    @Extension(url = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/ihe-designationType", definedLocally = false)
    @Description(shortDefinition = "Clinical code of the List")
    private List<CodeableConcept> designationType;

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(designationType);
    }

    public List<CodeableConcept> getDesignationType() {
        if (designationType == null) {
            designationType = new ArrayList<>();
        }
        return designationType;
    }

    public CodeableConcept addDesignationType() {
        CodeableConcept codeableConcept = new CodeableConcept();
        addDesignationType(codeableConcept);
        return codeableConcept;
    }

    public T addDesignationType(CodeableConcept codeableConcept) {
        if (codeableConcept != null) {
            if (this.designationType == null)
                this.designationType = new ArrayList<>();
            this.designationType.add(codeableConcept);
        }
        return (T)this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if
     *         it does not already exist
     */
    public CodeableConcept getDesignationTypeFirstRep() {
        if (getDesignationType().isEmpty()) {
            addDesignationType();
        }
        return getDesignationType().get(0);
    }

    public boolean hasDesignationType() {
        if (this.designationType == null)
            return false;
        for (CodeableConcept item : this.designationType)
            if (!item.isEmpty())
                return true;
        return false;
    }

    /**
     * Adds an identifier to be a EntryUuid as required by the profile
     * @param uuid uuid
     * @return this object
     */
    public T setEntryUuidIdentifier(UUID uuid) {
        getIdentifier().add(new EntryUuidIdentifier(uuid));
        return (T)this;
    }

    @Override
    public void copyValues(ListResource dst) {
        super.copyValues(dst);
        MhdList<T> mhdList = (MhdList<T>) dst;
        if (designationType != null) {
            mhdList.designationType = new ArrayList<>();
            for (CodeableConcept i : designationType)
                mhdList.designationType.add(i.copy());
        }
    }

    @Override
    public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
            return false;
        if (!(other_ instanceof MhdList))
            return false;
        MhdList<T> o = (MhdList<T>) other_;
        return compareDeep(designationType, o.designationType, true);
    }

}
