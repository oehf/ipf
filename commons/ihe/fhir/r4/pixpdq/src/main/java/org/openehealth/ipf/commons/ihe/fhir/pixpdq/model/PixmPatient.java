/*
 * Copyright 2026 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.pixpdq.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PixmProfile;

/**
 * Patient as sent by the Patient Identity Source in the Patient Identity Feed FHIR [ITI-104]
 * transaction. The PIXm Patient profile is derived from the PDQm Patient profile, and requires
 * a name.
 *
 * @author Christian Ohr
 * @since 6.0
 */
@ResourceDef(name = "Patient", id = "PixmPatient", profile = PixmProfile.PIXM_PATIENT_PROFILE)
public class PixmPatient extends PdqmPatient {

    public PixmPatient() {
        super();
        PixmProfile.PIXM_PATIENT.setProfile(this);
    }

    @Override
    public PixmPatient copy() {
        var copy = new PixmPatient();
        copyValues(copy);
        return copy;
    }

    /**
     * Marks this patient as subsumed by the surviving patient with the given identifier, as done
     * in the Resolve Duplicate Patient message of ITI-104. The subsumed patient is set inactive, as the
     * PDQm Patient profile requires {@code active} to be present along with a link.
     *
     * @param survivingPatientIdentifier identifier of the surviving patient
     * @return this patient
     */
    public PixmPatient addReplacedByLink(Identifier survivingPatientIdentifier) {
        setActive(false);
        addLink()
            .setType(LinkType.REPLACEDBY)
            .setOther(new Reference().setIdentifier(survivingPatientIdentifier));
        return this;
    }
}
