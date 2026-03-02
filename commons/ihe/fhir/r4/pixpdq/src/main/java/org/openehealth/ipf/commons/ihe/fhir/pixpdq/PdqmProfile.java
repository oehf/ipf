/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.pixpdq;

import ca.uhn.fhir.context.FhirContext;
import lombok.Getter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Resource;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmMatchInputParameters;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmMatchInputPatient;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmMatchOutputBundle;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmPatient;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.QueryPatientResourceResponseMessage;
import org.openehealth.ipf.commons.ihe.fhir.support.IheFhirProfile;

import java.util.List;
import java.util.Optional;

public enum PdqmProfile implements Pdqm320, IheFhirProfile {

    // Resource profiles

    PDQM_PATIENT(
        PdqmProfile.PDQM_PATIENT_PROFILE,
        PdqmPatient.class),

    PDQM_MATCH_INPUT_PATIENT(
        PdqmProfile.PDQM_MATCH_INPUT_PATIENT_PROFILE,
        PdqmMatchInputPatient.class),

    // Bundle profiles

    ITI78_QUERY_PATIENT_RESOURCE_RESPONSE_MESSAGE(
        PdqmProfile.ITI78_QUERY_PATIENT_RESOURCE_RESPONSE_MESSAGE_PROFILE,
        QueryPatientResourceResponseMessage.class),

    ITI119_MATCH_OUTPUT_BUNDLE(
        PdqmProfile.ITI119_MATCH_OUTPUT_BUNDLE_PROFILE,
        PdqmMatchOutputBundle.class),

    // Parameters profiles (for $match operation)

    ITI119_MATCH_INPUT_PARAMETERS(
        PdqmProfile.ITI119_MATCH_INPUT_PARAMETERS_PROFILE,
        PdqmMatchInputParameters.class);

    // Resource Profile URLs

    public static final String PDQM_PATIENT_PROFILE = "https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.Patient";
    public static final String PDQM_MATCH_INPUT_PATIENT_PROFILE = "https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.MatchInputPatient";

    // Bundle Profile URLs

    public static final String ITI78_QUERY_PATIENT_RESOURCE_RESPONSE_MESSAGE_PROFILE = "https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.QueryPatientResourceResponseMessage";
    public static final String ITI119_MATCH_OUTPUT_BUNDLE_PROFILE = "https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.MatchOutputBundle";

    // Parameters Profile URLs

    public static final String ITI119_MATCH_INPUT_PARAMETERS_PROFILE = "https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.MatchInputParameters";

    // Extension URLs (Datatypes)

    public static final String MOTHERS_MAIDEN_NAME_EXTENSION = "http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName";
    public static final String GENDER_IDENTITY_EXTENSION = "http://hl7.org/fhir/StructureDefinition/individual-genderIdentity";
    public static final String PRONOUNS_EXTENSION = "http://hl7.org/fhir/StructureDefinition/individual-pronouns";
    public static final String RECORDED_SEX_OR_GENDER_EXTENSION = "http://hl7.org/fhir/StructureDefinition/individual-recordedSexOrGender";
    public static final String BIRTH_PLACE_EXTENSION = "http://hl7.org/fhir/StructureDefinition/birthPlace";
    public static final String CITIZENSHIP_EXTENSION = "http://hl7.org/fhir/StructureDefinition/patient-citizenship";
    public static final String RELIGION_EXTENSION = "http://hl7.org/fhir/StructureDefinition/patient-religion";
    public static final String RACE_EXTENSION = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race";
    public static final String ETHNICITY_EXTENSION = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity";


    @Getter
    private final String url;

    @Getter
    private final Class<? extends IBaseResource> resourceClass;


    PdqmProfile(String url, Class<? extends IBaseResource> resourceClass) {
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

    public boolean hasProfile(Resource resource) {
        return resource.getMeta().hasProfile(url);
    }

    /**
     * Registers all the profiles and implementing classes in the {@link FhirContext}
     *
     * @param fhirContext FhirContext
     */
    public static void registerDefaultTypes(FhirContext fhirContext) {
        IheFhirProfile.registerProfiles(fhirContext, PdqmProfile.class);
    }

    public static Optional<PdqmProfile> profileForResource(IBaseResource resource) {
        return IheFhirProfile.profileForResource(resource, PdqmProfile.class);
    }

    public static Optional<PdqmProfile> profileForUrl(String url) {
        return IheFhirProfile.profileForUrl(url, PdqmProfile.class);
    }

}
