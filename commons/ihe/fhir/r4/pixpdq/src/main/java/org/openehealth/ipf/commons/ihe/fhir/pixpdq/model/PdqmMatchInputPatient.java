/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.pixpdq.model;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.*;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Pdqm320;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * FHIR Patient resource implementation for ITI-119 Patient Demographics Match input as defined by the PDQm specification.
 * <p>
 * This class extends the standard FHIR R4 Patient resource and serves as the input parameter for the $match operation
 * in the Patient Demographics Query for Mobile (PDQm) profile. It includes PDQm-specific extensions for demographic
 * matching, including mother's maiden name, gender identity, pronouns, and recorded sex or gender.
 * </p>
 * <p>
 * This resource is used within the {@link PdqmMatchInputParameters} to provide patient demographics for matching
 * against a patient registry. The server will use these demographics to find potential matches and return them
 * in a {@link PdqmMatchOutputBundle}.
 * </p>
 * <p>
 * Unlike {@link PdqmPatient}, this class focuses on the subset of
 * demographic elements relevant for patient matching operations and does not include backward-compatible
 * extensions such as birthplace, citizenship, religion, race, or ethnicity.
 * </p>
 *
 * @author Christian Ohr
 * @since 5.2
 * @see PdqmMatchInputParameters
 * @see PdqmMatchOutputBundle
 * @see PdqmProfile#PDQM_MATCH_INPUT_PATIENT_PROFILE
 */
@ResourceDef(name = "Patient", id = "pdqm", profile = PdqmProfile.PDQM_MATCH_INPUT_PATIENT_PROFILE)
public class PdqmMatchInputPatient extends Patient implements Pdqm320 {

    @Child(name = "mothersMaidenName")
    @Extension(url = PdqmProfile.MOTHERS_MAIDEN_NAME_EXTENSION, definedLocally = false)
    @Description(shortDefinition = "Mother's maiden name of a patient")
    private StringType mothersMaidenName;

    @Child(name = "genderIdentity", max = Child.MAX_UNLIMITED)
    @Extension(url = PdqmProfile.GENDER_IDENTITY_EXTENSION, definedLocally = false)
    @Description(shortDefinition = "The individual's gender identity")
    private List<GenderIdentity> genderIdentity;

    @Child(name = "pronouns", max = Child.MAX_UNLIMITED)
    @Extension(url = PdqmProfile.PRONOUNS_EXTENSION, definedLocally = false)
    @Description(shortDefinition = "The pronouns to use when communicating about an individual.")
    private List<Pronouns> pronouns;

    @Child(name = "recordedSexOrGender", max = Child.MAX_UNLIMITED)
    @Extension(url = PdqmProfile.RECORDED_SEX_OR_GENDER_EXTENSION, definedLocally = false)
    @Description(shortDefinition = "A recorded sex or gender property for the individual")
    private List<RecordedSexOrGender> recordedSexOrGender;

    public PdqmMatchInputPatient() {
        super();
        PdqmProfile.PDQM_MATCH_INPUT_PATIENT.setProfile(this);
    }

    @Override
    public PdqmMatchInputPatient copy() {
        var copy = new PdqmMatchInputPatient();
        copyValues(copy);
        return copy;
    }

    public void copyValues(PdqmMatchInputPatient dst) {
        super.copyValues(dst);
        dst.mothersMaidenName = mothersMaidenName != null ? mothersMaidenName.copy() : null;
        dst.genderIdentity = copyList(genderIdentity);
        dst.pronouns = copyList(pronouns);
        dst.recordedSexOrGender = copyList(recordedSexOrGender);
    }

    private <T extends BackboneElement> List<T> copyList(List<T> source) {
        if (source == null) {
            return null;
        }
        List<T> destination = new ArrayList<>(source.size());
        for (T item : source) {
            @SuppressWarnings("unchecked")
            T copiedItem = (T) item.copy();
            destination.add(copiedItem);
        }
        return destination;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(mothersMaidenName, genderIdentity, pronouns, recordedSexOrGender);
    }

    public String getMothersMaidenName() {
        return mothersMaidenName == null ? null : mothersMaidenName.getValue();
    }

    public PdqmMatchInputPatient setMothersMaidenName(String mothersMaidenName) {
        if (mothersMaidenName == null) {
            this.mothersMaidenName = null;
        } else {
            if (this.mothersMaidenName == null) {
                this.mothersMaidenName = new StringType();
            }
            this.mothersMaidenName.setValue(mothersMaidenName);
        }
        return this;
    }

    public boolean hasMothersMaidenName() {
        return mothersMaidenName != null && !mothersMaidenName.isEmpty();
    }

    public StringType getMothersMaidenNameElement() {
        if (mothersMaidenName == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create PdqmMatchInputPatient.mothersMaidenName");
            } else if (Configuration.doAutoCreate()) {
                mothersMaidenName = new StringType();
            }
        }
        return mothersMaidenName;
    }

    public PdqmMatchInputPatient setMothersMaidenNameElement(StringType mothersMaidenName) {
        this.mothersMaidenName = mothersMaidenName;
        return this;
    }

    // Gender Identity methods
    public List<GenderIdentity> getGenderIdentity() {
        if (genderIdentity == null) {
            genderIdentity = new ArrayList<>();
        }
        return genderIdentity;
    }

    public PdqmMatchInputPatient setGenderIdentity(List<GenderIdentity> genderIdentity) {
        this.genderIdentity = genderIdentity;
        return this;
    }

    public boolean hasGenderIdentity() {
        return genderIdentity != null && genderIdentity.stream().anyMatch(item -> !item.isEmpty());
    }

    public GenderIdentity addGenderIdentity() {
        var t = new GenderIdentity();
        getGenderIdentity().add(t);
        return t;
    }

    public PdqmMatchInputPatient addGenderIdentity(GenderIdentity genderIdentity) {
        if (genderIdentity != null) {
            getGenderIdentity().add(genderIdentity);
        }
        return this;
    }

    public GenderIdentity getGenderIdentityFirstRep() {
        if (getGenderIdentity().isEmpty()) {
            addGenderIdentity();
        }
        return getGenderIdentity().get(0);
    }

    // Pronouns methods
    public List<Pronouns> getPronouns() {
        if (pronouns == null) {
            pronouns = new ArrayList<>();
        }
        return pronouns;
    }

    public PdqmMatchInputPatient setPronouns(List<Pronouns> pronouns) {
        this.pronouns = pronouns;
        return this;
    }

    public boolean hasPronouns() {
        return pronouns != null && pronouns.stream().anyMatch(item -> !item.isEmpty());
    }

    public Pronouns addPronoun() {
        var t = new Pronouns();
        getPronouns().add(t);
        return t;
    }

    public PdqmMatchInputPatient addPronoun(Pronouns pronouns) {
        if (pronouns != null) {
            getPronouns().add(pronouns);
        }
        return this;
    }

    public Pronouns getPronounsFirstRep() {
        if (getPronouns().isEmpty()) {
            addPronoun();
        }
        return getPronouns().get(0);
    }

    // Recorded Sex or Gender methods
    public List<RecordedSexOrGender> getRecordedSexOrGender() {
        if (recordedSexOrGender == null) {
            recordedSexOrGender = new ArrayList<>();
        }
        return recordedSexOrGender;
    }

    public PdqmMatchInputPatient setRecordedSexOrGender(List<RecordedSexOrGender> recordedSexOrGender) {
        this.recordedSexOrGender = recordedSexOrGender;
        return this;
    }

    public boolean hasRecordedSexOrGender() {
        return recordedSexOrGender != null && recordedSexOrGender.stream().anyMatch(item -> !item.isEmpty());
    }

    public RecordedSexOrGender addRecordedSexOrGender() {
        var t = new RecordedSexOrGender();
        getRecordedSexOrGender().add(t);
        return t;
    }

    public PdqmMatchInputPatient addRecordedSexOrGender(RecordedSexOrGender recordedSexOrGender) {
        if (recordedSexOrGender != null) {
            getRecordedSexOrGender().add(recordedSexOrGender);
        }
        return this;
    }

    public RecordedSexOrGender getRecordedSexOrGenderFirstRep() {
        if (getRecordedSexOrGender().isEmpty()) {
            addRecordedSexOrGender();
        }
        return getRecordedSexOrGender().get(0);
    }

}
