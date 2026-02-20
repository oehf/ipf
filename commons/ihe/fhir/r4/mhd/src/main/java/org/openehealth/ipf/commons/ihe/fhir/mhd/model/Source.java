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

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Reference;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.AUTHOR_ORG_PROFILE;

/**
 * Source data type extension. Unfortunately we cannot register this datatype with the FhirContext
 * right now as parsing/rendering won't work anymore afterwards. It can be used, however, to
 * assemble resources.
 */
@DatatypeDef(name = "Source", profileOf = Reference.class, isSpecialization = true)
public class Source extends Reference {


    @Override
    public boolean isEmpty() {
        return super.isEmpty() && !hasAuthorOrg();
    }

    public Source setAuthorOrg(Reference authorOrg) {
        removeExtension(AUTHOR_ORG_PROFILE);
        if (authorOrg != null) {
            addExtension(new Extension(AUTHOR_ORG_PROFILE, authorOrg));
        }
        return this;
    }

    public boolean hasAuthorOrg() {
        return hasExtension(AUTHOR_ORG_PROFILE);
    }

    public Reference getAuthorOrg() {
        Extension extension = getExtensionByUrl(AUTHOR_ORG_PROFILE);
        if (extension != null && extension.hasValue() && extension.getValue() instanceof Reference) {
            return (Reference) extension.getValue();
        }
        return null;
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
        if (!(other_ instanceof Source o))
            return false;
        return compareDeep(getAuthorOrg(), o.getAuthorOrg(), true);
    }

}
