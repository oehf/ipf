/*
 * Copyright 2016 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti65;

/**
 * @author Christian Ohr
 * @since 3.6
 */
public interface Iti65Constants {

    String ITI65_COMPREHENSIVE_METADATA_PROFILE = "http://ihe.net/fhir/StructureDefinition/IHE_MHD_Provide_Comprehensive_DocumentBundle";
    String ITI65_MINIMAL_METADATA_PROFILE = "http://ihe.net/fhir/StructureDefinition/IHE_MHD_Provide_Minimal_DocumentBundle";

    String ITI65_MINIMAL_DOCUMENT_MANIFEST_PROFILE = "http://ihe.net/fhir/StructureDefinition/IHE_MHD_Minimal_DocumentManifest";
    String ITI65_MINIMAL_DOCUMENT_REFERENCE_PROFILE = "http://ihe.net/fhir/StructureDefinition/IHE_MHD_Provide_Minimal_DocumentReference";

}
