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

interface MhdConstants {

    // Bundle Profiles V4

    String ITI65_MINIMAL_BUNDLE_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.Minimal.ProvideBundle";
    String ITI65_COMPREHENSIVE_BUNDLE_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.Comprehensive.ProvideBundle";
    String ITI65_UNCONTAINED_COMPREHENSIVE_BUNDLE_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.UnContained.Comprehensive.ProvideBundle";
    String ITI65_PROVIDE_DOCUMENT_BUNDLE_RESPONSE_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.ProvideDocumentBundleResponse";
    String ITI66_FIND_DOCUMENT_LISTS_RESPONSE_BUNDLE_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.FindDocumentListsResponseMessage";
    String ITI67_FIND_DOCUMENT_REFERENCES_RESPONSE_BUNDLE_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.FindDocumentReferencesResponseMessage";
    String ITI67_FIND_DOCUMENT_REFERENCES_COMPREHENSIVE_RESPONSE_BUNDLE_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.FindDocumentReferencesComprehensiveResponseMessage";

    // DocumentManifest/List profiles

    String MHD_LIST_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.List";
    String COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.Comprehensive.SubmissionSet";
    String UNCONTAINED_COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.UnContained.Comprehensive.SubmissionSet";
    String MINIMAL_SUBMISSIONSET_TYPE_LIST_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.Minimal.SubmissionSet";

    // List Profile

    String MINIMAL_FOLDER_TYPE_LIST_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.Minimal.Folder";
    String COMPREHENSIVE_FOLDER_TYPE_LIST_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.Comprehensive.Folder";

    // DocumentReference profiles

    String COMPREHENSIVE_DOCUMENT_REFERENCE_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.Comprehensive.DocumentReference";
    String UNCONTAINED_COMPREHENSIVE_DOCUMENT_REFERENCE_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.UnContained.Comprehensive.DocumentReference";
    String MINIMAL_DOCUMENT_REFERENCE_PROFILE =  "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.Minimal.DocumentReference";
    String SIMPLIFIED_PUBLISH_DOCUMENT_REFERENCE_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.SimplifiedPublish.DocumentReference";

    // Parameters

    String DOCUMENT_REFERENCE_PATCH_PARAMETERS_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.Patch.Parameters";
    String GENERATE_METADATA_PARAMETERS_IN_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.GenerateMetadata.Parameters.In";
    String GENERATE_METADATA_PARAMETERS_OUT_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.GenerateMetadata.Parameters.Out";

    // Datatypes

    String DESIGNATION_TYPE_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/ihe-designationType";
    String AUTHOR_ORG_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/ihe-authorOrg";
    String INTENDED_RECIPIENT_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/ihe-intendedRecipient";
    String SOURCE_ID_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/ihe-sourceId";
    String SUBMISSIONSET_UNIQUE_IDENTIFIER_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.SubmissionSetUniqueIdIdentifier";
    String UNIQUE_ID_IDENTIFIER_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.UniqueIdIdentifier";
    String ENTRY_UUID_IDENTIFIER_PROFILE = "https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.EntryUUID.Identifier";
}
