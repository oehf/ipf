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
import org.hl7.fhir.r4.model.Binary;
import org.openehealth.ipf.commons.ihe.fhir.mhd.Mhd421;
import org.openehealth.ipf.commons.ihe.fhir.support.FhirUtils;

import java.util.List;
import java.util.Optional;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.ITI65_UNCONTAINED_COMPREHENSIVE_BUNDLE;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile.ITI65_UNCONTAINED_COMPREHENSIVE_BUNDLE_PROFILE;

@ResourceDef(name = "Bundle", id = "mhdUncontainedComprehensiveBundle", profile = ITI65_UNCONTAINED_COMPREHENSIVE_BUNDLE_PROFILE)
public class UncontainedComprehensiveProvideDocumentBundle
    extends AbstractProvideDocumentBundle<UncontainedComprehensiveProvideDocumentBundle>
    implements Mhd421 {

    public UncontainedComprehensiveProvideDocumentBundle() {
        super();
        setType(BundleType.TRANSACTION);
        ITI65_UNCONTAINED_COMPREHENSIVE_BUNDLE.setProfile(this);
    }

    public UncontainedComprehensiveSubmissionSetList getSubmissionSet() {
        return FhirUtils.getResource(this, UncontainedComprehensiveSubmissionSetList.class);
    }

    public List<UncontainedComprehensiveDocumentReference> getDocumentReferences() {
        return FhirUtils.getResources(this, UncontainedComprehensiveDocumentReference.class);
    }

    public Optional<Binary> getBinary() {
        return FhirUtils.getOptionalResource(this, Binary.class);
    }

    public List<FolderList> getFolders() {
        return FhirUtils.getResources(this, FolderList.class);
    }

}
