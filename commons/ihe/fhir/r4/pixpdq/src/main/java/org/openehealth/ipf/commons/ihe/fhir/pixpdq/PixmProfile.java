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
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmQueryParametersIn;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmQueryParametersOut;
import org.openehealth.ipf.commons.ihe.fhir.support.IheFhirProfile;

import java.util.List;
import java.util.Optional;

/**
 * Enumeration of PIXm profile definitions for ITI-83 (Mobile Patient Identifier Cross-reference Query).
 * <p>
 * This enum defines the FHIR profiles used in the PIXm transaction, including the Parameters
 * resources for input and output of the $ihe-pix operation.
 * </p>
 *
 * @author Christian Ohr
 * @since 5.2
 */
public enum PixmProfile implements Pixm310, IheFhirProfile {

    // Parameters profiles for ITI-83

    PIXM_QUERY_PARAMETERS_IN(
        PixmProfile.PIXM_QUERY_PARAMETERS_IN_PROFILE,
        PixmQueryParametersIn.class),

    PIXM_QUERY_PARAMETERS_OUT(
        PixmProfile.PIXM_QUERY_PARAMETERS_OUT_PROFILE,
        PixmQueryParametersOut.class);

    // Parameters Profile URLs

    public static final String PIXM_QUERY_PARAMETERS_IN_PROFILE = "https://profiles.ihe.net/ITI/PIXm/StructureDefinition/IHE.PIXm.Query.Parameters.In";
    public static final String PIXM_QUERY_PARAMETERS_OUT_PROFILE = "https://profiles.ihe.net/ITI/PIXm/StructureDefinition/IHE.PIXm.Query.Parameters.Out";

    @Getter
    private final String url;

    @Getter
    private final Class<? extends IBaseResource> resourceClass;


    PixmProfile(String url, Class<? extends IBaseResource> resourceClass) {
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
