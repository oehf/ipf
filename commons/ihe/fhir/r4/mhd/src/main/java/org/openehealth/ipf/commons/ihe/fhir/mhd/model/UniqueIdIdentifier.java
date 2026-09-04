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
import org.hl7.fhir.r4.model.Identifier;

// https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.UniqueIdIdentifier

@DatatypeDef(name = "UniqueIdIdentifier", profileOf = Identifier.class, isSpecialization = true)
public class UniqueIdIdentifier extends Identifier {

    public UniqueIdIdentifier() {
        super();
        // MHD up to 4.2.3 required the use, 4.2.4 requires the type (CP-ITI-1328-01). Both are
        // written, so that the identifier conforms to either version of the profile.
        setUse(IdentifierUse.USUAL);
        setType(MhdIdentifierType.UNIQUE_ID.toCodeableConcept());
    }

    @Override
    public UniqueIdIdentifier copy() {
        var dst = new UniqueIdIdentifier();
        copyValues(dst);
        return dst;
    }
}
