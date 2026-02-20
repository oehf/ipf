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
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ListResource;

import java.util.ArrayList;
import java.util.List;

public abstract class FolderList<T extends FolderList<T>> extends MhdList<T> {

    public FolderList() {
        super();
        setCode(new CodeableConcept().addCoding(FOLDER_LIST_CODING));
    }

    @Child(name = "designationType", type = { CodeableConcept.class }, order = 1)
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
        var codeableConcept = new CodeableConcept();
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
        for (var item : this.designationType)
            if (!item.isEmpty())
                return true;
        return false;
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

    @Override
    public void copyValues(ListResource dst) {
        super.copyValues(dst);
        var folderList = (FolderList<T>) dst;
        if (designationType != null) {
            folderList.designationType = new ArrayList<>();
            for (var i : designationType)
                folderList.designationType.add(i.copy());
        }
    }

    @Override
    public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
            return false;
        if (!(other_ instanceof MhdList))
            return false;
        var o = (FolderList<T>) other_;
        return compareDeep(designationType, o.designationType, true);
    }

    public static final Coding FOLDER_LIST_CODING = new Coding(
        "https://profiles.ihe.net/ITI/MHD/CodeSystem/MHDlistTypes",
        "folder",
        "Folder as a FHIR List"
    );
}
