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
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.util.ElementUtil;
import lombok.Getter;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Reference;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.AUTHOR_ORG_PROFILE;

/**
 * Source data type extension. Unfortunately we cannot register this datatype with the FhirCOntext
 * right now as parsing/rendering won't work anymore afterwards. It can be used, however, to
 * assemble resources.
 */
@DatatypeDef(name = "Source", profileOf = Reference.class, isSpecialization = true)
public class Source extends Reference {

    @Child(name = "authorOrg")
    @Extension(url = AUTHOR_ORG_PROFILE, definedLocally = false)
    private Reference authorOrg;

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(authorOrg);
    }

    public Source setAuthorOrg(Reference authorOrg) {
        this.authorOrg = authorOrg;
        return this;
    }

    public boolean hasAuthorOrg() {
        return authorOrg != null && !authorOrg.isEmpty();
    }

    public Reference getAuthorOrg() {
        return authorOrg;
    }

    @Override
    public Source copy() {
        var dst = new Source();
        copyValues(dst);
        return dst;
    }

    @Override
    public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
            return false;
        if (!(other_ instanceof Source))
            return false;
        Source o = (Source) other_;
        return compareDeep(authorOrg, o.authorOrg, true);
    }

    @Override
    public void copyValues(Reference dst) {
        super.copyValues(dst);
        ((Source)dst).authorOrg = authorOrg == null ? null : authorOrg.copy();
    }
}
