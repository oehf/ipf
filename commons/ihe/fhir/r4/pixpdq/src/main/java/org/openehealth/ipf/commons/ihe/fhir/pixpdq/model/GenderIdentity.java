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
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.StringType;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmProfile;

/**
 * Gender Identity extension datatype for PDQm profiles.
 * <p>
 * This datatype represents an individual's gender identity, including the value,
 * time period during which it applies, and optional comments.
 * </p>
 *
 * @author Christian Ohr
 * @since 5.2
 * @see PdqmProfile#GENDER_IDENTITY_EXTENSION
 */
@Block
public class GenderIdentity extends BackboneElement {

    @Child(name = "value", min = 1)
    @Extension(url = "value", definedLocally = false)
    @Description(shortDefinition = "The individual's gender identity")
    private CodeableConcept value;

    @Child(name = "period")
    @Extension(url = "period", definedLocally = false)
    @Description(shortDefinition = "The time period during which the gender identity applies to the individual")
    private Period period;

    @Child(name = "comment")
    @Extension(url = "comment", definedLocally = false)
    @Description(shortDefinition = "Text to further explain the use of the specified gender identity")
    private StringType comment;

    @Override
    public GenderIdentity copy() {
        var copy = new GenderIdentity();
        copyValues(copy);
        return copy;
    }

    public void copyValues(GenderIdentity dst) {
        super.copyValues(dst);
        dst.value = value != null ? value.copy() : null;
        dst.period = period != null ? period.copy() : null;
        dst.comment = comment != null ? comment.copy() : null;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(value, period, comment);
    }

    public CodeableConcept getValue() {
        if (value == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create GenderIdentity.value");
            } else if (Configuration.doAutoCreate()) {
                value = new CodeableConcept();
            }
        }
        return value;
    }

    public GenderIdentity setValue(CodeableConcept value) {
        this.value = value;
        return this;
    }

    public boolean hasValue() {
        return value != null && !value.isEmpty();
    }

    public Period getPeriod() {
        if (period == null) {
            if (Configuration.errorOnAutoCreate()) {
                throw new Error("Attempt to auto-create GenderIdentity.period");
            } else if (Configuration.doAutoCreate()) {
                period = new Period();
            }
        }
        return period;
    }

    public GenderIdentity setPeriod(Period period) {
        this.period = period;
        return this;
    }

    public boolean hasPeriod() {
        return period != null && !period.isEmpty();
    }

    public String getComment() {
        return comment == null ? null : comment.getValue();
    }

    public GenderIdentity setComment(String comment) {
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
                throw new Error("Attempt to auto-create GenderIdentity.comment");
            } else if (Configuration.doAutoCreate()) {
                comment = new StringType();
            }
        }
        return comment;
    }

    public GenderIdentity setCommentElement(StringType comment) {
        this.comment = comment;
        return this;
    }
}