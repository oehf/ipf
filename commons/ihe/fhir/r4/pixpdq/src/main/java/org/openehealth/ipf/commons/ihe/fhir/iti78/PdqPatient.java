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

package org.openehealth.ipf.commons.ihe.fhir.iti78;

import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Patient as defined by the PDQm specification plus some of the most common extensions
 * This extends the default patient resource with the following attributes:
 * <ul>
 * <li>birthplace</li>
 * <li>mothersMaidenName</li>
 * <li>citizenship</li>
 * <li>religion</li>
 * <li>race</li>
 * <li>ethnicity</li>
 * </ul>
 *
 * @author Christian Ohr
 * @since 3.6
 */
@ResourceDef(name = "Patient", id = "pdqm", profile = "http://org.openehealth.ipd/fhir/StructureDefinition/IHE.PDQm.PatientResource")
public class PdqPatient extends Patient {

    // Search Parameters

    public static final TokenClientParam IDENTIFIER = new TokenClientParam(Patient.SP_IDENTIFIER);
    public static final TokenClientParam ACTIVE = new TokenClientParam(Patient.SP_ACTIVE);
    public static final StringClientParam FAMILY = new StringClientParam(Patient.SP_FAMILY);
    public static final StringClientParam GIVEN = new StringClientParam(Patient.SP_GIVEN);
    public static final DateClientParam BIRTHDATE = new DateClientParam(Patient.SP_BIRTHDATE);
    public static final StringClientParam ADDRESS = new StringClientParam(Patient.SP_ADDRESS);
    public static final TokenClientParam GENDER = new TokenClientParam(Patient.SP_GENDER);
    public static final TokenClientParam RESOURCE_IDENTIFIER = new TokenClientParam(IAnyResource.SP_RES_ID);
    public static final StringClientParam TELECOM = new StringClientParam(Patient.SP_TELECOM);

    @Child(name = "birthPlace")
    @Extension(url = "http://hl7.org/fhir/StructureDefinition/birthPlace", isModifier = false, definedLocally = false)
    @Description(shortDefinition = "The registered place of birth of the patient. A system may use the address.text if they don't store the birthPlace address in discrete elements")
    private Address birthPlace;

    @Child(name = "mothersMaidenName")
    @Extension(url = "http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName", isModifier = false, definedLocally = false)
    @Description(shortDefinition = "Mother's maiden name of a patient")
    private HumanName mothersMaidenName;

    @Child(name = "citizenship", max = Child.MAX_UNLIMITED)
    @Extension(url = "http://hl7.org/fhir/StructureDefinition/patient-citizenship", isModifier = false, definedLocally = false)
    @Description(shortDefinition = "The citizenship of the patient")
    private List<Citizenship> citizenship;

    @Child(name = "religion", max = Child.MAX_UNLIMITED)
    @Extension(url = "http://hl7.org/fhir/StructureDefinition/patient-religion", isModifier = false, definedLocally = false)
    @Description(shortDefinition = "The patient's professed religious affiliations")
    private List<CodeableConcept> religion;

    @Child(name = "race")
    @Extension(url = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race", isModifier = false, definedLocally = false)
    @Description(shortDefinition = "Concepts classifying  the person into a named category of humans sharing common history, traits, geographical origin or nationality")
    private Race race;

    @Child(name = "ethnicity")
    @Extension(url = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity", isModifier = false, definedLocally = false)
    @Description(shortDefinition = "Concepts classifying the person into a named category of humans sharing common history, traits, geographical origin or nationality.")
    private Ethnicity ethnicity;

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(birthPlace, mothersMaidenName, citizenship, religion, race, ethnicity);
    }

    public Address getBirthPlace() {
        if (birthPlace == null) {
            birthPlace = new Address();
        }
        return birthPlace;
    }

    public void setBirthPlace(Address birthPlace) {
        this.birthPlace = birthPlace;
    }

    public boolean hasBirthPlace() {
        return this.birthPlace != null && !this.birthPlace.isEmpty();
    }

    public HumanName getMothersMaidenName() {
        if (mothersMaidenName == null) {
            mothersMaidenName = new HumanName();
        }
        return mothersMaidenName;
    }

    public void setMothersMaidenName(HumanName mothersMaidenName) {
        this.mothersMaidenName = mothersMaidenName;
    }

    public boolean hasMothersMaidenName() {
        return this.mothersMaidenName != null && !this.mothersMaidenName.isEmpty();
    }

    public List<Citizenship> getCitizenship() {
        if (citizenship == null) {
            citizenship = new ArrayList<>();
        }
        return citizenship;
    }

    public void setCitizenship(List<Citizenship> citizenship) {
        this.citizenship = citizenship;
    }

    public Citizenship addCitizenship() {
        Citizenship t = new Citizenship();
        if (this.citizenship == null)
            this.citizenship = new ArrayList<>();
        this.citizenship.add(t);
        return t;
    }

    public Patient addCitizenship(Citizenship t) {
        if (t == null)
            return this;
        if (this.citizenship == null)
            this.citizenship = new ArrayList<>();
        this.citizenship.add(t);
        return this;
    }

    public boolean hasCitizenship() {
        if (this.citizenship == null)
            return false;
        for (Citizenship item : this.citizenship)
            if (!item.isEmpty())
                return true;
        return false;
    }

    public List<CodeableConcept> getReligion() {
        if (religion == null) {
            religion = new ArrayList<>();
        }
        return religion;
    }

    public void setReligion(List<CodeableConcept> religion) {
        this.religion = religion;
    }

    public boolean hasReligion() {
        if (this.religion == null)
            return false;
        for (CodeableConcept item : this.religion)
            if (!item.isEmpty())
                return true;
        return false;
    }

    public CodeableConcept addReligion() {
        CodeableConcept t = new CodeableConcept();
        if (this.religion == null)
            this.religion = new ArrayList<>();
        this.religion.add(t);
        return t;
    }

    public Patient addReligion(CodeableConcept t) {
        if (t == null)
            return this;
        if (this.religion == null)
            this.religion = new ArrayList<>();
        this.religion.add(t);
        return this;
    }

    public Race getRace() {
        if (race == null) {
            race = new Race();
        }
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public boolean hasRace() {
        return this.race != null && !this.race.isEmpty();
    }

    public Ethnicity getEthnicity() {
        if (ethnicity == null) {
            ethnicity = new Ethnicity();
        }
        return ethnicity;
    }

    public void setEthnicity(Ethnicity ethnicity) {
        this.ethnicity = ethnicity;
    }

    public boolean hasEthnicity() {
        return this.ethnicity != null && !this.ethnicity.isEmpty();
    }

    @Block
    public static class Citizenship extends BackboneElement {

        @Child(name = "code")
        @Extension(url = "http://hl7.org/fhir/StructureDefinition/patient-citizenship/code", definedLocally = false, isModifier = false)
        private CodeableConcept code;

        @Child(name = "period")
        @Extension(url = "http://hl7.org/fhir/StructureDefinition/patient-citizenship/period", definedLocally = false, isModifier = false)
        private Period period;

        @Override
        public Citizenship copy() {
            Citizenship copy = new Citizenship();
            copy.code = code;
            copy.period = period;
            return copy;
        }

        @Override
        public boolean isEmpty() {
            return super.isEmpty() && ElementUtil.isEmpty(code, period);
        }

        public CodeableConcept getCode() {
            return code;
        }

        public Period getPeriod() {
            return period;
        }

        public void setCode(CodeableConcept code) {
            this.code = code;
        }

        public boolean hasCode() {
            return code != null && !code.isEmpty();
        }

        public void setPeriod(Period period) {
            this.period = period;
        }

        public boolean hasPeriod() {
            return period != null && !period.isEmpty();
        }
    }

    @Block
    public static class Race extends BackboneElement {

        @Child(name = "ombCategory")
        @Extension(url = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race/ombCategory", definedLocally = false, isModifier = false)
        private Coding ombCategory;

        @Child(name = "detailed")
        @Extension(url = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race/detailed", definedLocally = false, isModifier = false)
        private Coding detailed;

        @Child(name = "text")
        @Extension(url = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race/text", definedLocally = false, isModifier = false)
        private StringType text;

        @Override
        public Race copy() {
            Race copy = new Race();
            copy.ombCategory = ombCategory;
            copy.detailed = detailed;
            copy.text = text;
            return copy;
        }

        @Override
        public boolean isEmpty() {
            return super.isEmpty() && ElementUtil.isEmpty(ombCategory, detailed, text);
        }

        public Coding getOmbCategory() {
            return ombCategory;
        }

        public Coding getDetailed() {
            return detailed;
        }

        public StringType getText() {
            return text;
        }

        public void setOmbCategory(Coding ombCategory) {
            this.ombCategory = ombCategory;
        }

        public void setDetailed(Coding detailed) {
            this.detailed = detailed;
        }

        public void setText(StringType text) {
            this.text = text;
        }

        public boolean hasOmbCategory() {
            return ombCategory != null && !ombCategory.isEmpty();
        }

        public boolean hasDetailed() {
            return detailed != null && !detailed.isEmpty();
        }
        public boolean hasText() {
            return text != null && !text.isEmpty();
        }
    }

    @Block
    public static class Ethnicity extends BackboneElement {

        @Child(name = "ombCategory")
        @Extension(url = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity/ombCategory", definedLocally = false, isModifier = false)
        private Coding ombCategory;

        @Child(name = "detailed")
        @Extension(url = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity/detailed", definedLocally = false, isModifier = false)
        private Coding detailed;

        @Child(name = "text")
        @Extension(url = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity/text", definedLocally = false, isModifier = false)
        private StringType text;

        @Override
        public Ethnicity copy() {
            Ethnicity copy = new Ethnicity();
            copy.ombCategory = ombCategory;
            copy.detailed = detailed;
            copy.text = text;
            return copy;
        }

        @Override
        public boolean isEmpty() {
            return super.isEmpty() && ElementUtil.isEmpty(ombCategory, detailed, text);
        }

        public Coding getOmbCategory() {
            return ombCategory;
        }

        public Coding getDetailed() {
            return detailed;
        }

        public StringType getText() {
            return text;
        }

        public void setOmbCategory(Coding ombCategory) {
            this.ombCategory = ombCategory;
        }

        public void setDetailed(Coding detailed) {
            this.detailed = detailed;
        }

        public void setText(StringType text) {
            this.text = text;
        }

        public boolean hasOmbCategory() {
            return ombCategory != null && !ombCategory.isEmpty();
        }

        public boolean hasDetailed() {
            return detailed != null && !detailed.isEmpty();
        }
        public boolean hasText() {
            return text != null && !text.isEmpty();
        }
    }
}
