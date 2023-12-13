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

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.*;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.COMPREHENSIVE_DOCUMENT_REFERENCE;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.COMPREHENSIVE_DOCUMENT_REFERENCE_PROFILE;


@ResourceDef(name = "DocumentReference", id = "mhdComprehensiveDocumentReference", profile = COMPREHENSIVE_DOCUMENT_REFERENCE_PROFILE)
public class ComprehensiveDocumentReference extends UncontainedComprehensiveDocumentReference<ComprehensiveDocumentReference> {

    public ComprehensiveDocumentReference() {
        super();
        COMPREHENSIVE_DOCUMENT_REFERENCE.setProfile(this);
    }

    public ComprehensiveDocumentReference addAuthor(Practitioner practioner) {
        super.addAuthor(new Reference(practioner));
        return this;
    }

    public ComprehensiveDocumentReference addAuthor(Patient patient) {
        super.addAuthor(new Reference(patient));
        return this;
    }

    public ComprehensiveDocumentReference addAuthor(PractitionerRole practitionerRole) {
        super.addAuthor(new Reference(practitionerRole));
        return this;
    }

    public ComprehensiveDocumentReference addAuthor(Organization organization) {
        super.addAuthor(new Reference(organization));
        return this;
    }

    public ComprehensiveDocumentReference addAuthor(Device device) {
        super.addAuthor(new Reference(device));
        return this;
    }

    public ComprehensiveDocumentReference addAuthor(RelatedPerson relatedPerson) {
        super.addAuthor(new Reference(relatedPerson));
        return this;
    }

    public ComprehensiveDocumentReference setAuthenticator(Practitioner practitioner) {
        super.setAuthenticator(new Reference(practitioner));
        return this;
    }

    public ComprehensiveDocumentReference setAuthenticator(PractitionerRole practitionerRole) {
        super.setAuthenticator(new Reference(practitionerRole));
        return this;
    }

    public ComprehensiveDocumentReference setAuthenticator(Organization organization) {
        super.setAuthenticator(new Reference(organization));
        return this;
    }

    public ComprehensiveDocumentReference setSourcePatientInfo(Patient patient) {
        getContext().setSourcePatientInfo(new Reference(patient));
        return this;
    }

}
