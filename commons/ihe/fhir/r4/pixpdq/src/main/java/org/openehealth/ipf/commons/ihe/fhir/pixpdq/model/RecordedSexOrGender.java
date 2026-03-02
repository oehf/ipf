/*
 * Copyright 2026 the original author or authors.
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
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.BackboneElement;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Configuration;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmProfile;

/**
 * Recorded Sex or Gender extension datatype for PDQm profiles.
 * <p>
 * This datatype represents a recorded sex or gender property for an individual,
 * including the value, type, effective period, acquisition date, source document,
 * jurisdiction, and optional comments.
 * </p>
 *
 * @author Christian Ohr
 * @since 5.2
 * @see PdqmProfile#RECORDED_SEX_OR_GENDER_EXTENSION
 */
@Block
public class RecordedSexOrGender extends BackboneElement {

    @Child(name = "value", min = 1)
    @Extension(url = "value", definedLocally = false)
    @Description(shortDefinition = "The recorded sex or gender value")
    private CodeableConcept value;

    @Child(name = "type")
    @Extension(url = "type", definedLocally = false)
    @Description(shortDefinition = "The type of sex or gender that is recorded")
    private CodeableConcept type;

    @Child(name = "effectivePeriod")
    @Extension(url = "effectivePeriod", definedLocally = false)
    @Description(shortDefinition = "The time period during which the recorded sex or gender applies")
    private Period effectivePeriod;

    @Child(name = "acquisitionDate")
    @Extension(url = "acquisitionDate", definedLocally = false)
    @Description(shortDefinition = "The date when the sex or gender was acquired or recorded")
    private DateTimeType acquisitionDate;

    @Child(name = "sourceDocument")
    @Extension(url = "sourceDocument", definedLocally = false)
    @Description(shortDefinition = "Reference to the source document")
    private Reference sourceDocument;

    @Child(name = "sourceField")
    @Extension(url = "sourceField", definedLocally = false)
    @Description(shortDefinition = "The name of the field in the source document")
    private StringType sourceField;

    @Child(name = "jurisdiction")
    @Extension(url = "jurisdiction", definedLocally = false)
    @Description(shortDefinition = "The jurisdiction where the sex or gender was recorded")
    private CodeableConcept jurisdiction;

    @Child(name = "comment")
    @Extension(url = "comment", definedLocally = false)
    @Description(shortDefinition = "Text to further explain the use of the recorded sex or gender")
    private StringType comment;

    @Override
    public RecordedSexOrGender copy() {
        var copy = new RecordedSexOrGender();
        copyValues(copy);
        return copy;
    }

    public void copyValues(RecordedSexOrGender dst) {
        super.copyValues(dst);
        dst.value = value != null ? value.copy() : null;
        dst.type = type != null ? type.copy() : null;
        dst.effectivePeriod = effectivePeriod != null ? effectivePeriod.copy() : null;
        dst.acquisitionDate = acquisitionDate != null ? acquisitionDate.copy() : null;
        dst.sourceDocument = sourceDocument != null ? sourceDocument.copy() : null;
        dst.sourceField = sourceField != null ? sourceField.copy() : null;
        dst.jurisdiction = jurisdiction != null ? jurisdiction.copy() : null;
        dst.comment = comment != null ? comment.copy() : null;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(value, type, effectivePeriod, acquisitionDate,
                sourceDocument, sourceField, jurisdiction, comment);
    }

    // Value methods
    public CodeableConcept getValue() {
        if (value == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create RecordedSexOrGender.value");
            } else if (Configuration.doAutoCreate()) {
                value = new CodeableConcept();
            }
        }
        return value;
    }

    public RecordedSexOrGender setValue(CodeableConcept value) {
        this.value = value;
        return this;
    }

    public boolean hasValue() {
        return value != null && !value.isEmpty();
    }

    // Type methods
    public CodeableConcept getType() {
        if (type == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create RecordedSexOrGender.type");
            } else if (Configuration.doAutoCreate()) {
                type = new CodeableConcept();
            }
        }
        return type;
    }

    public RecordedSexOrGender setType(CodeableConcept type) {
        this.type = type;
        return this;
    }

    public boolean hasType() {
        return type != null && !type.isEmpty();
    }

    // Effective Period methods
    public Period getEffectivePeriod() {
        if (effectivePeriod == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create RecordedSexOrGender.effectivePeriod");
            } else if (Configuration.doAutoCreate()) {
                effectivePeriod = new Period();
            }
        }
        return effectivePeriod;
    }

    public RecordedSexOrGender setEffectivePeriod(Period effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
        return this;
    }

    public boolean hasEffectivePeriod() {
        return effectivePeriod != null && !effectivePeriod.isEmpty();
    }

    // Acquisition Date methods
    public DateTimeType getAcquisitionDateElement() {
        if (acquisitionDate == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create RecordedSexOrGender.acquisitionDate");
            } else if (Configuration.doAutoCreate()) {
                acquisitionDate = new DateTimeType();
            }
        }
        return acquisitionDate;
    }

    public RecordedSexOrGender setAcquisitionDateElement(DateTimeType acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
        return this;
    }

    public RecordedSexOrGender setAcquisitionDate(String acquisitionDate) {
        if (acquisitionDate == null) {
            this.acquisitionDate = null;
        } else {
            if (this.acquisitionDate == null) {
                this.acquisitionDate = new DateTimeType();
            }
            this.acquisitionDate.setValueAsString(acquisitionDate);
        }
        return this;
    }

    public boolean hasAcquisitionDate() {
        return acquisitionDate != null && !acquisitionDate.isEmpty();
    }

    // Source Document methods
    public Reference getSourceDocument() {
        if (sourceDocument == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create RecordedSexOrGender.sourceDocument");
            } else if (Configuration.doAutoCreate()) {
                sourceDocument = new Reference();
            }
        }
        return sourceDocument;
    }

    public RecordedSexOrGender setSourceDocument(Reference sourceDocument) {
        this.sourceDocument = sourceDocument;
        return this;
    }

    public boolean hasSourceDocument() {
        return sourceDocument != null && !sourceDocument.isEmpty();
    }

    // Source Field methods
    public String getSourceField() {
        return sourceField == null ? null : sourceField.getValue();
    }

    public RecordedSexOrGender setSourceField(String sourceField) {
        if (sourceField == null) {
            this.sourceField = null;
        } else {
            if (this.sourceField == null) {
                this.sourceField = new StringType();
            }
            this.sourceField.setValue(sourceField);
        }
        return this;
    }

    public boolean hasSourceField() {
        return sourceField != null && !sourceField.isEmpty();
    }

    public StringType getSourceFieldElement() {
        if (sourceField == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create RecordedSexOrGender.sourceField");
            } else if (Configuration.doAutoCreate()) {
                sourceField = new StringType();
            }
        }
        return sourceField;
    }

    public RecordedSexOrGender setSourceFieldElement(StringType sourceField) {
        this.sourceField = sourceField;
        return this;
    }

    // Jurisdiction methods
    public CodeableConcept getJurisdiction() {
        if (jurisdiction == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create RecordedSexOrGender.jurisdiction");
            } else if (Configuration.doAutoCreate()) {
                jurisdiction = new CodeableConcept();
            }
        }
        return jurisdiction;
    }

    public RecordedSexOrGender setJurisdiction(CodeableConcept jurisdiction) {
        this.jurisdiction = jurisdiction;
        return this;
    }

    public boolean hasJurisdiction() {
        return jurisdiction != null && !jurisdiction.isEmpty();
    }

    // Comment methods
    public String getComment() {
        return comment == null ? null : comment.getValue();
    }

    public RecordedSexOrGender setComment(String comment) {
        if (comment == null) {
            this.comment = null;
        } else {
            if (this.comment == null) {
                this.comment = new StringType();
            }
            this.comment.setValue(comment);
        }
        return this;
    }

    public boolean hasComment() {
        return comment != null && !comment.isEmpty();
    }

    public StringType getCommentElement() {
        if (comment == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create RecordedSexOrGender.comment");
            } else if (Configuration.doAutoCreate()) {
                comment = new StringType();
            }
        }
        return comment;
    }

    public RecordedSexOrGender setCommentElement(StringType comment) {
        this.comment = comment;
        return this;
    }
}