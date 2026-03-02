/*
 * Copyright 2015 the original author or authors.
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

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.BackboneElement;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Configuration;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.StringType;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Pdqm320;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * FHIR Patient resource implementation for the PDQm (Patient Demographics Query for Mobile) profile.
 * <p>
 * This class extends the standard FHIR R4 Patient resource with additional extensions defined in the
 * IHE PDQm profile, including gender identity, pronouns, recorded sex or gender, and other demographic
 * extensions. It also includes backward-compatible extensions such as birthplace, citizenship, religion,
 * race, and ethnicity that were present in previous versions but are not part of the strict PDQm profile.
 * </p>
 * <p>
 * The class provides search parameters and accessor methods for all supported extensions, following
 * FHIR resource patterns for element access and manipulation.
 * </p>
 *
 * @see org.hl7.fhir.r4.model.Patient
 * @see PdqmProfile
 */
@ResourceDef(name = "Patient", id = "PDQmPatient", profile = PdqmProfile.PDQM_PATIENT_PROFILE)
public class PdqmPatient extends Patient implements Pdqm320 {

    public static final TokenClientParam IDENTIFIER = new TokenClientParam(Patient.SP_IDENTIFIER);
    public static final TokenClientParam ACTIVE = new TokenClientParam(Patient.SP_ACTIVE);
    public static final StringClientParam FAMILY = new StringClientParam(Patient.SP_FAMILY);
    public static final StringClientParam GIVEN = new StringClientParam(Patient.SP_GIVEN);
    public static final DateClientParam BIRTHDATE = new DateClientParam(Patient.SP_BIRTHDATE);
    public static final StringClientParam ADDRESS = new StringClientParam(Patient.SP_ADDRESS);
    public static final TokenClientParam GENDER = new TokenClientParam(Patient.SP_GENDER);
    public static final TokenClientParam RESOURCE_IDENTIFIER = new TokenClientParam(IAnyResource.SP_RES_ID);
    public static final StringClientParam TELECOM = new StringClientParam(Patient.SP_TELECOM);

    @Child(name = "mothersMaidenName")
    @Extension(url = PdqmProfile.MOTHERS_MAIDEN_NAME_EXTENSION, definedLocally = false)
    @Description(shortDefinition = "Mother's Maiden name")
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

    // The attributes below are NOT part of the PdqmPatient profile, but they were present in the previous version
    // of the Patient class, so we leave it in for backward-compatibility reasons. Don't populate them if you
    // want to strictly follow the PDQm profile.

    @Child(name = "birthPlace")
    @Extension(url = PdqmProfile.BIRTH_PLACE_EXTENSION, definedLocally = false)
    @Description(shortDefinition = "The registered place of birth of the patient")
    private Address birthPlace;

    @Child(name = "citizenship", max = Child.MAX_UNLIMITED)
    @Extension(url = PdqmProfile.CITIZENSHIP_EXTENSION, definedLocally = false)
    @Description(shortDefinition = "The citizenship of the patient")
    private List<Citizenship> citizenship;

    @Child(name = "religion", max = Child.MAX_UNLIMITED)
    @Extension(url = PdqmProfile.RELIGION_EXTENSION, definedLocally = false)
    @Description(shortDefinition = "The patient's professed religious affiliations")
    private List<CodeableConcept> religion;

    @Child(name = "race")
    @Extension(url = PdqmProfile.RACE_EXTENSION, definedLocally = false)
    @Description(shortDefinition = "Concepts classifying  the person into a named category of humans sharing common history, traits, geographical origin or nationality")
    private Race race;

    @Child(name = "ethnicity")
    @Extension(url = PdqmProfile.ETHNICITY_EXTENSION, definedLocally = false)
    @Description(shortDefinition = "Concepts classifying the person into a named category of humans sharing common history, traits, geographical origin or nationality.")
    private Ethnicity ethnicity;

    public PdqmPatient() {
        super();
        PdqmProfile.PDQM_PATIENT.setProfile(this);
    }

    @Override
    public PdqmPatient copy() {
        var copy = new PdqmPatient();
        copyValues(copy);
        return copy;
    }

    public void copyValues(PdqmPatient dst) {
        super.copyValues(dst);
        if (mothersMaidenName != null) {
            dst.mothersMaidenName = mothersMaidenName.copy();
        }
        dst.genderIdentity = copyList(genderIdentity);
        dst.pronouns = copyList(pronouns);
        dst.recordedSexOrGender = copyList(recordedSexOrGender);
        if (birthPlace != null) {
            dst.birthPlace = birthPlace.copy();
        }
        dst.citizenship = copyList(citizenship);
        dst.religion = copyList(religion);
        if (race != null) {
            dst.race = race.copy();
        }
        if (ethnicity != null) {
            dst.ethnicity = ethnicity.copy();
        }
    }

    private <T extends org.hl7.fhir.r4.model.Base> List<T> copyList(List<T> source) {
        if (source == null) {
            return null;
        }
        var result = new ArrayList<T>();
        for (T item : source) {
            result.add((T) item.copy());
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(mothersMaidenName, genderIdentity, pronouns, recordedSexOrGender,
            birthPlace, citizenship, religion, race, ethnicity);
    }

    // Mother's Maiden Name methods
    public String getMothersMaidenName() {
        return mothersMaidenName == null ? null : mothersMaidenName.getValue();
    }

    public PdqmPatient setMothersMaidenName(String mothersMaidenName) {
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
                throw new Error("Attempt to auto-create PDQPatient.mothersMaidenName");
            } else if (Configuration.doAutoCreate()) {
                mothersMaidenName = new StringType();
            }
        }
        return mothersMaidenName;
    }

    public PdqmPatient setMothersMaidenNameElement(StringType mothersMaidenName) {
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

    public PdqmPatient setGenderIdentity(List<GenderIdentity> genderIdentity) {
        this.genderIdentity = genderIdentity;
        return this;
    }

    public boolean hasGenderIdentity() {
        return hasNonEmptyItems(genderIdentity);
    }

    public GenderIdentity addGenderIdentity() {
        var t = new GenderIdentity();
        getGenderIdentity().add(t);
        return t;
    }

    public PdqmPatient addGenderIdentity(GenderIdentity genderIdentity) {
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

    public PdqmPatient setPronouns(List<Pronouns> pronouns) {
        this.pronouns = pronouns;
        return this;
    }

    public boolean hasPronouns() {
        return hasNonEmptyItems(pronouns);
    }

    public Pronouns addPronoun() {
        var t = new Pronouns();
        getPronouns().add(t);
        return t;
    }

    public PdqmPatient addPronoun(Pronouns pronouns) {
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

    public PdqmPatient setRecordedSexOrGender(List<RecordedSexOrGender> recordedSexOrGender) {
        this.recordedSexOrGender = recordedSexOrGender;
        return this;
    }

    public boolean hasRecordedSexOrGender() {
        return hasNonEmptyItems(recordedSexOrGender);
    }

    public RecordedSexOrGender addRecordedSexOrGender() {
        var t = new RecordedSexOrGender();
        getRecordedSexOrGender().add(t);
        return t;
    }

    public PdqmPatient addRecordedSexOrGender(RecordedSexOrGender recordedSexOrGender) {
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

    // Birth Place methods
    public Address getBirthPlace() {
        if (birthPlace == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create PdqPatient.birthPlace");
            } else if (Configuration.doAutoCreate()) {
                birthPlace = new Address();
            }
        }
        return birthPlace;
    }

    public PdqmPatient setBirthPlace(Address birthPlace) {
        this.birthPlace = birthPlace;
        return this;
    }

    public boolean hasBirthPlace() {
        return birthPlace != null && !birthPlace.isEmpty();
    }

    // Citizenship methods
    public List<Citizenship> getCitizenship() {
        if (citizenship == null) {
            citizenship = new ArrayList<>();
        }
        return citizenship;
    }

    public PdqmPatient setCitizenship(List<Citizenship> citizenship) {
        this.citizenship = citizenship;
        return this;
    }

    public boolean hasCitizenship() {
        return hasNonEmptyItems(citizenship);
    }

    public Citizenship addCitizenship() {
        var t = new Citizenship();
        getCitizenship().add(t);
        return t;
    }

    public PdqmPatient addCitizenship(Citizenship citizenship) {
        if (citizenship != null) {
            getCitizenship().add(citizenship);
        }
        return this;
    }

    public Citizenship getCitizenshipFirstRep() {
        if (getCitizenship().isEmpty()) {
            addCitizenship();
        }
        return getCitizenship().get(0);
    }

    // Religion methods
    public List<CodeableConcept> getReligion() {
        if (religion == null) {
            religion = new ArrayList<>();
        }
        return religion;
    }

    public PdqmPatient setReligion(List<CodeableConcept> religion) {
        this.religion = religion;
        return this;
    }

    public boolean hasReligion() {
        return hasNonEmptyItems(religion);
    }

    public CodeableConcept addReligion() {
        var t = new CodeableConcept();
        getReligion().add(t);
        return t;
    }

    public PdqmPatient addReligion(CodeableConcept religion) {
        if (religion != null) {
            getReligion().add(religion);
        }
        return this;
    }

    public CodeableConcept getReligionFirstRep() {
        if (getReligion().isEmpty()) {
            addReligion();
        }
        return getReligion().get(0);
    }

    // Race methods
    public Race getRace() {
        if (race == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create PdqPatient.race");
            } else if (Configuration.doAutoCreate()) {
                race = new Race();
            }
        }
        return race;
    }

    public PdqmPatient setRace(Race race) {
        this.race = race;
        return this;
    }

    public boolean hasRace() {
        return race != null && !race.isEmpty();
    }

    // Ethnicity methods
    public Ethnicity getEthnicity() {
        if (ethnicity == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create PdqPatient.ethnicity");
            } else if (Configuration.doAutoCreate()) {
                ethnicity = new Ethnicity();
            }
        }
        return ethnicity;
    }

    public PdqmPatient setEthnicity(Ethnicity ethnicity) {
        this.ethnicity = ethnicity;
        return this;
    }

    public boolean hasEthnicity() {
        return ethnicity != null && !ethnicity.isEmpty();
    }

    // Helper method to check if a list has non-empty items
    private <T extends org.hl7.fhir.r4.model.Base> boolean hasNonEmptyItems(List<T> list) {
        if (list == null) {
            return false;
        }
        for (var item : list) {
            if (!item.isEmpty()) {
                return true;
            }
        }
        return false;
    }




    @Block
    public static class Citizenship extends BackboneElement {

        @Child(name = "code")
        @Extension(url = "code", definedLocally = false)
        private CodeableConcept code;

        @Child(name = "period")
        @Extension(url = "period", definedLocally = false)
        private Period period;

        @Override
        public Citizenship copy() {
            var copy = new Citizenship();
            copyValues(copy);
            return copy;
        }

        public void copyValues(Citizenship dst) {
            super.copyValues(dst);
            if (code != null) {
                dst.code = code.copy();
            }
            if (period != null) {
                dst.period = period.copy();
            }
        }

        @Override
        public boolean isEmpty() {
            return super.isEmpty() && ElementUtil.isEmpty(code, period);
        }

        public CodeableConcept getCode() {
            if (code == null) {
                if (Configuration.errorOnAutoCreate()) {
                    throw new Error("Attempt to auto-create Citizenship.code");
                } else if (Configuration.doAutoCreate()) {
                    code = new CodeableConcept();
                }
            }
            return code;
        }

        public Citizenship setCode(CodeableConcept code) {
            this.code = code;
            return this;
        }

        public boolean hasCode() {
            return code != null && !code.isEmpty();
        }

        public Period getPeriod() {
            if (period == null) {
                if (Configuration.errorOnAutoCreate()) {
                    throw new Error("Attempt to auto-create Citizenship.period");
                } else if (Configuration.doAutoCreate()) {
                    period = new Period();
                }
            }
            return period;
        }

        public Citizenship setPeriod(Period period) {
            this.period = period;
            return this;
        }

        public boolean hasPeriod() {
            return period != null && !period.isEmpty();
        }
    }

    @Block
    public static class Race extends BackboneElement {

        @Child(name = "ombCategory")
        @Extension(url = "ombCategory", definedLocally = false)
        private Coding ombCategory;

        @Child(name = "detailed")
        @Extension(url = "detailed", definedLocally = false)
        private Coding detailed;

        @Child(name = "text")
        @Extension(url = "text", definedLocally = false)
        private StringType text;

        @Override
        public Race copy() {
            var copy = new Race();
            copyValues(copy);
            return copy;
        }

        public void copyValues(Race dst) {
            super.copyValues(dst);
            if (ombCategory != null) {
                dst.ombCategory = ombCategory.copy();
            }
            if (detailed != null) {
                dst.detailed = detailed.copy();
            }
            if (text != null) {
                dst.text = text.copy();
            }
        }

        @Override
        public boolean isEmpty() {
            return super.isEmpty() && ElementUtil.isEmpty(ombCategory, detailed, text);
        }

        public Coding getOmbCategory() {
            if (ombCategory == null) {
                if (Configuration.errorOnAutoCreate()) {
                    throw new Error("Attempt to auto-create Race.ombCategory");
                } else if (Configuration.doAutoCreate()) {
                    ombCategory = new Coding();
                }
            }
            return ombCategory;
        }

        public Race setOmbCategory(Coding ombCategory) {
            this.ombCategory = ombCategory;
            return this;
        }

        public boolean hasOmbCategory() {
            return ombCategory != null && !ombCategory.isEmpty();
        }

        public Coding getDetailed() {
            if (detailed == null) {
                if (Configuration.errorOnAutoCreate()) {
                    throw new Error("Attempt to auto-create Race.detailed");
                } else if (Configuration.doAutoCreate()) {
                    detailed = new Coding();
                }
            }
            return detailed;
        }

        public Race setDetailed(Coding detailed) {
            this.detailed = detailed;
            return this;
        }

        public boolean hasDetailed() {
            return detailed != null && !detailed.isEmpty();
        }

        public StringType getText() {
            if (text == null) {
                if (Configuration.errorOnAutoCreate()) {
                    throw new Error("Attempt to auto-create Race.text");
                } else if (Configuration.doAutoCreate()) {
                    text = new StringType();
                }
            }
            return text;
        }

        public Race setText(StringType text) {
            this.text = text;
            return this;
        }

        public boolean hasText() {
            return text != null && !text.isEmpty();
        }
    }

    @Block
    public static class Ethnicity extends BackboneElement {

        @Child(name = "ombCategory")
        @Extension(url = "ombCategory", definedLocally = false)
        private Coding ombCategory;

        @Child(name = "detailed")
        @Extension(url = "detailed", definedLocally = false)
        private Coding detailed;

        @Child(name = "text")
        @Extension(url = "text", definedLocally = false)
        private StringType text;

        @Override
        public Ethnicity copy() {
            var copy = new Ethnicity();
            copyValues(copy);
            return copy;
        }

        public void copyValues(Ethnicity dst) {
            super.copyValues(dst);
            if (ombCategory != null) {
                dst.ombCategory = ombCategory.copy();
            }
            if (detailed != null) {
                dst.detailed = detailed.copy();
            }
            if (text != null) {
                dst.text = text.copy();
            }
        }

        @Override
        public boolean isEmpty() {
            return super.isEmpty() && ElementUtil.isEmpty(ombCategory, detailed, text);
        }

        public Coding getOmbCategory() {
            if (ombCategory == null) {
                if (Configuration.errorOnAutoCreate()) {
                    throw new Error("Attempt to auto-create Ethnicity.ombCategory");
                } else if (Configuration.doAutoCreate()) {
                    ombCategory = new Coding();
                }
            }
            return ombCategory;
        }

        public Ethnicity setOmbCategory(Coding ombCategory) {
            this.ombCategory = ombCategory;
            return this;
        }

        public boolean hasOmbCategory() {
            return ombCategory != null && !ombCategory.isEmpty();
        }

        public Coding getDetailed() {
            if (detailed == null) {
                if (Configuration.errorOnAutoCreate()) {
                    throw new Error("Attempt to auto-create Ethnicity.detailed");
                } else if (Configuration.doAutoCreate()) {
                    detailed = new Coding();
                }
            }
            return detailed;
        }

        public Ethnicity setDetailed(Coding detailed) {
            this.detailed = detailed;
            return this;
        }

        public boolean hasDetailed() {
            return detailed != null && !detailed.isEmpty();
        }

        public StringType getText() {
            if (text == null) {
                if (Configuration.errorOnAutoCreate()) {
                    throw new Error("Attempt to auto-create Ethnicity.text");
                } else if (Configuration.doAutoCreate()) {
                    text = new StringType();
                }
            }
            return text;
        }

        public Ethnicity setText(StringType text) {
            this.text = text;
            return this;
        }

        public boolean hasText() {
            return text != null && !text.isEmpty();
        }
    }
}
