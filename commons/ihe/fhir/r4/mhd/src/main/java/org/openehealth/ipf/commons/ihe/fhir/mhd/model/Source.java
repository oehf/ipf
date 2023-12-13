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
import org.hl7.fhir.r4.model.Reference;

@Getter
@DatatypeDef(name = "Source", profileOf = Reference.class)
public class Source extends Reference {

    @Child(name = "authorOrg")
    @Extension(url = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/ihe-authorOrg", definedLocally = false)
    private Reference authorOrg;

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(authorOrg, reference);
    }

    public Source setAuthorOrg(Reference authorOrg) {
        this.authorOrg = authorOrg;
        return this;
    }

    @Override
    public Source setReference(String value) {
        super.setReference(value);
        return this;
    }

    public boolean hasAuthorOrg() {
        return authorOrg != null && !authorOrg.isEmpty();
    }

}
