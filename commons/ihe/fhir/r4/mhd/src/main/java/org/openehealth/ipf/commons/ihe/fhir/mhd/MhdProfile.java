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
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Resource;
import org.openehealth.ipf.commons.ihe.fhir.mhd.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum MhdProfile implements MhdConstants, Mhd421 {

    // Bundle Profiles V421

    ITI65_MINIMAL_BUNDLE(
        ITI65_MINIMAL_BUNDLE_PROFILE,
        MinimalProvideDocumentBundle.class,
        "IHE.MHD.Minimal.ProvideBundle"),

    ITI65_COMPREHENSIVE_BUNDLE(
        ITI65_COMPREHENSIVE_BUNDLE_PROFILE,
        ComprehensiveProvideDocumentBundle.class,
        "IHE.MHD.Comprehensive.ProvideBundle"),

    ITI65_UNCONTAINED_COMPREHENSIVE_BUNDLE(
        ITI65_UNCONTAINED_COMPREHENSIVE_BUNDLE_PROFILE,
        UncontainedComprehensiveProvideDocumentBundle.class,
        "IHE.MHD.UnContained.Comprehensive.ProvideBundle"),

    ITI65_PROVIDE_DOCUMENT_BUNDLE_RESPONSE(
        ITI65_PROVIDE_DOCUMENT_BUNDLE_RESPONSE_PROFILE,
        ProvideDocumentBundleResponse.class,
        "IHE.MHD.ProvideDocumentBundleResponse"),

    ITI66_FIND_DOCUMENT_LISTS_RESPONSE_BUNDLE(
        ITI66_FIND_DOCUMENT_LISTS_RESPONSE_BUNDLE_PROFILE,
        FindDocumentListsResponseBundle.class,
        "IHE.MHD.FindDocumentListsResponseMessage"),

    ITI67_FIND_DOCUMENT_REFERENCES_RESPONSE_BUNDLE(
        ITI67_FIND_DOCUMENT_REFERENCES_RESPONSE_BUNDLE_PROFILE,
        FindMinimalDocumentReferencesResponseBundle.class,
        "IHE.MHD.FindDocumentReferencesResponseMessage"
    ),

    ITI67_FIND_DOCUMENT_REFERENCES_COMPREHENSIVE_RESPONSE_BUNDLE(
        ITI67_FIND_DOCUMENT_REFERENCES_COMPREHENSIVE_RESPONSE_BUNDLE_PROFILE,
        FindComprehensiveDocumentReferencesResponseBundle.class,
        "IHE.MHD.FindDocumentReferencesComprehensiveResponseMessage"),

    // List profiles v421

    MHD_LIST(
        MHD_LIST_PROFILE,
        MhdList.class,
        "IHE.MHD.List"),

    COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST(
        COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST_PROFILE,
        ComprehensiveSubmissionSetList.class,
        "IHE.MHD.Comprehensive.SubmissionSet"),

    UNCONTAINED_COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST(
        UNCONTAINED_COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST_PROFILE,
        UncontainedComprehensiveProvideDocumentBundle.class,
        "IHE.MHD.UnContained.Comprehensive.SubmissionSet"),

    MINIMAL_SUBMISSIONSET_TYPE_LIST(
        MINIMAL_SUBMISSIONSET_TYPE_LIST_PROFILE,
        MinimalSubmissionSetList.class,
        "IHE.MHD.Minimal.SubmissionSet"),

    MINIMAL_FOLDER_TYPE_LIST(
        MINIMAL_FOLDER_TYPE_LIST_PROFILE,
        MinimalFolderList.class,
        "IHE.MHD.Minimal.Folder"),

    COMPREHENSIVE_FOLDER_TYPE_LIST(
        COMPREHENSIVE_FOLDER_TYPE_LIST_PROFILE,
        ComprehensiveFolderList.class,
        "IHE.MHD.Comprehensive.Folder"),

    // DocumentReference profiles v421

    COMPREHENSIVE_DOCUMENT_REFERENCE(
        COMPREHENSIVE_DOCUMENT_REFERENCE_PROFILE,
        ComprehensiveDocumentReference.class,
        "IHE.MHD.Comprehensive.DocumentReference"),

    UNCONTAINED_COMPREHENSIVE_DOCUMENT_REFERENCE(
        UNCONTAINED_COMPREHENSIVE_DOCUMENT_REFERENCE_PROFILE,
        UncontainedComprehensiveDocumentReference.class,
        "IHE.MHD.UnContained.Comprehensive.DocumentReference"),

    MINIMAL_DOCUMENT_REFERENCE(
        MINIMAL_DOCUMENT_REFERENCE_PROFILE,
        MinimalDocumentReference.class,
        "IHE.MHD.Minimal.DocumentReference"),

    SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE(
        SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE_PROFILE,
        SimplifiedPublishDocumentReference.class,
        "IHE.MHD.SimplifiedPublish.DocumentReference"),

    // Parameters

    DOCUMENT_REFERENCE_PATCH_PARAMETERS(
        DOCUMENT_REFERENCE_PATCH_PARAMETERS_PROFILE,
        DocumentReferencePatchParameters.class,
        "IHE.MHD.Patch.Parameters"),

    GENERATE_METADATA_PARAMETERS_IN(
        GENERATE_METADATA_PARAMETERS_IN_PROFILE,
        GenerateMetadataInParameters.class,
        "IHE.MHD.GenerateMetadata.Parameters.In"),

    GENERATE_METADATA_PARAMETERS_OUT(
        GENERATE_METADATA_PARAMETERS_OUT_PROFILE,
        GenerateMetadataOutParameters.class,
        "IHE.MHD.GenerateMetadata.Parameters.Out"),

    // Datatypes etc.

    DESIGNATION_TYPE(
        DESIGNATION_TYPE_PROFILE,
        null,
        "ihe-designationType"),

    AUTHOR_ORG(
        AUTHOR_ORG_PROFILE,
        null,
        "ihe-authorOrg"),

    INTENDED_RECIPIENT(
        INTENDED_RECIPIENT_PROFILE,
        null,
        "ihe-intendedRecipient"),

    SOURCE_ID(
        SOURCE_ID_PROFILE,
        null,
        "ihe-sourceId"),

    SUBMISSIONSET_UNIQUE_IDENTIFIER(
        SUBMISSIONSET_UNIQUE_IDENTIFIER_PROFILE,
        null,
        "IHE.MHD.SubmissionSetUniqueIdIdentifier"),

    UNIQUE_ID_IDENTIFIER(
        UNIQUE_ID_IDENTIFIER_PROFILE,
        null,
        "IHE.MHD.UniqueIdIdentifier"),

    ENTRY_UUID_IDENTIFIER(
        ENTRY_UUID_IDENTIFIER_PROFILE,
        null,
        "IHE.MHD.EntryUUID.Identifier");


    @Getter
    private final String url;

    private final Class<? extends IBaseResource> resourceClass;

    @Getter
    private final String structureDefinitionName;


    MhdProfile(String url, Class<? extends IBaseResource> resourceClass, String structureDefinitionName) {
        this.url = url;
        this.resourceClass = resourceClass;
        this.structureDefinitionName = structureDefinitionName;
    }

    /**
     * Set the Meta/Profile of the resource
     *
     * @param resource FHIR resource
     */
    public void setProfile(Resource resource) {
        resource.getMeta().setProfile(List.of(new CanonicalType(url)));
    }

    public boolean hasProfile(Resource resource) {
        return resource.getMeta().hasProfile(url);
    }

    public String getStructureDefinitionResourceName() {
        return "StructureDefinition-" + structureDefinitionName + ".xml";
    }

    /**
     * Registers all the profiles and implementing classes in the {@link FhirContext}
     *
     * @param fhirContext FhirContext
     */
    public static void registerDefaultTypes(FhirContext fhirContext) {
        Arrays.stream(MhdProfile.values())
            .filter(profile -> profile.resourceClass != null)
            .forEach(profile -> fhirContext.setDefaultTypeForProfile(profile.url, profile.resourceClass));
    }

    public static Optional<MhdProfile> profileForResource(IBaseResource resource) {
        return resource.getMeta().getProfile().stream()
            .map(IPrimitiveType::getValue)
            .findFirst()
            .flatMap(MhdProfile::profileForUrl);
    }

    public static Optional<MhdProfile> profileForUrl(String url) {
        return Arrays.stream(MhdProfile.values())
            .filter(p -> p.url.equalsIgnoreCase(url))
            .findFirst();
    }

}
