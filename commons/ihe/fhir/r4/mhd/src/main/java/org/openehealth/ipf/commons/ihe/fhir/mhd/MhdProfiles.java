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
package org.openehealth.ipf.commons.ihe.fhir.mhd;

import ca.uhn.fhir.context.FhirContext;
import lombok.Getter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Resource;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.*;

import java.util.Arrays;
import java.util.List;

@Getter
public enum MhdProfiles implements MhdConstants {


    // Bundle Profiles V4

    ITI65_MINIMAL_BUNDLE(
        ITI65_MINIMAL_BUNDLE_PROFILE, MinimalProvideDocumentBundle.class),
    ITI65_COMPREHENSIVE_BUNDLE(
        ITI65_COMPREHENSIVE_BUNDLE_PROFILE, ComprehensiveProvideDocumentBundle.class),
    ITI65_UNCONTAINED_COMPREHENSIVE_BUNDLE(
        ITI65_UNCONTAINED_COMPREHENSIVE_BUNDLE_PROFILE, UncontainedComprehensiveProvideDocumentBundle.class),
    ITI65_PROVIDE_DOCUMENT_BUNDLE_RESPONSE(
        ITI65_PROVIDE_DOCUMENT_BUNDLE_RESPONSE_PROFILE, ProvideDocumentBundleResponse.class),
    ITI66_FIND_DOCUMENT_LISTS_RESPONSE_BUNDLE(
        ITI66_FIND_DOCUMENT_LISTS_RESPONSE_BUNDLE_PROFILE, FindDocumentListsResponseBundle.class),
    ITI67_FIND_DOCUMENT_REFERENCES_RESPONSE_BUNDLE(
        ITI67_FIND_DOCUMENT_REFERENCES_RESPONSE_BUNDLE_PROFILE, FindMinimalDocumentReferencesResponseBundle.class),
    ITI67_FIND_DOCUMENT_REFERENCES_COMPREHENSIVE_RESPONSE_BUNDLE(
        ITI67_FIND_DOCUMENT_REFERENCES_COMPREHENSIVE_RESPONSE_BUNDLE_PROFILE, FindComprehensiveDocumentReferencesResponseBundle.class),

    // List profiles

    MHD_LIST(MHD_LIST_PROFILE, MhdList.class),
    COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST(COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST_PROFILE, ComprehensiveSubmissionSetList.class),
    UNCONTAINED_COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST(UNCONTAINED_COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST_PROFILE, UncontainedComprehensiveProvideDocumentBundle.class),
    MINIMAL_SUBMISSIONSET_TYPE_LIST(MINIMAL_SUBMISSIONSET_TYPE_LIST_PROFILE, MinimalSubmissionSetList.class),

    // List Profile

    MINIMAL_FOLDER_TYPE_LIST(MINIMAL_FOLDER_TYPE_LIST_PROFILE, MinimalFolderList.class),
    COMPREHENSIVE_FOLDER_TYPE_LIST(COMPREHENSIVE_FOLDER_TYPE_LIST_PROFILE, ComprehensiveFolderList.class),

    // DocumentReference profiles

    COMPREHENSIVE_DOCUMENT_REFERENCE(COMPREHENSIVE_DOCUMENT_REFERENCE_PROFILE, ComprehensiveDocumentReference.class),
    UNCONTAINED_COMPREHENSIVE_DOCUMENT_REFERENCE(UNCONTAINED_COMPREHENSIVE_DOCUMENT_REFERENCE_PROFILE, UncontainedComprehensiveDocumentReference.class),
    MINIMAL_DOCUMENT_REFERENCE(MINIMAL_DOCUMENT_REFERENCE_PROFILE, MinimalDocumentReference.class),
    SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE(SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE_PROFILE, SimplifiedPublishDocumentReference.class);


    private final String url;

    private final Class<? extends IBaseResource> resourceClass;


    MhdProfiles(String url, Class<? extends IBaseResource> resourceClass) {
        this.url = url;
        this.resourceClass = resourceClass;
    }

    /**
     * Set the Meta/Profile of the resource
     *
     * @param resource FHIR resource
     */
    public void setProfile(Resource resource) {
        resource.getMeta().setProfile(List.of(new CanonicalType(url)));
    }

    /**
     * Registers all the profiles and implementing classes in the {@link FhirContext}
     *
     * @param fhirContext FhirContext
     */
    public static void registerDefaultTypes(FhirContext fhirContext) {
        Arrays.stream(MhdProfiles.values()).forEach(profile ->
            fhirContext.setDefaultTypeForProfile(profile.getUrl(), profile.resourceClass));
    }

}
