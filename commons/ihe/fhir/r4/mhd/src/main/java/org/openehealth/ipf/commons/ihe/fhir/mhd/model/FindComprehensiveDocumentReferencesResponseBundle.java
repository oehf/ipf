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
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;

import java.util.List;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfiles.ITI67_FIND_DOCUMENT_REFERENCES_COMPREHENSIVE_RESPONSE_BUNDLE;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfiles.ITI67_FIND_DOCUMENT_REFERENCES_COMPREHENSIVE_RESPONSE_BUNDLE_PROFILE;
import static org.openehealth.ipf.commons.ihe.fhir.support.FhirUtils.getResources;

@ResourceDef(name = "Bundle", id = "mhdComprehensiveDocumentReferenceResponseBundle", profile = ITI67_FIND_DOCUMENT_REFERENCES_COMPREHENSIVE_RESPONSE_BUNDLE_PROFILE)
public class FindComprehensiveDocumentReferencesResponseBundle extends Bundle {

    public FindComprehensiveDocumentReferencesResponseBundle() {
        super();
        setType(BundleType.SEARCHSET);
        ITI67_FIND_DOCUMENT_REFERENCES_COMPREHENSIVE_RESPONSE_BUNDLE.setProfile(this);
    }

    public List<ComprehensiveDocumentReference> getComprehensiveDocumentReferences() {
        return getResources(this, ComprehensiveDocumentReference.class);
    }

}
