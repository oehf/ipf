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
package org.openehealth.ipf.commons.ihe.fhir.support;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Common interface for IHE FHIR profile enumerations.
 * Provides default implementations for common profile operations.
 *
 * @since 5.2
 */
public interface IheFhirProfile {

    /**
     * Gets the canonical URL of the profile
     *
     * @return the profile URL
     */
    String getUrl();

    /**
     * Gets the resource class associated with this profile
     *
     * @return the resource class, or null if not applicable
     */
    Class<? extends IBaseResource> getResourceClass();

    /**
     * Set the Meta/Profile of the resource
     *
     * @param resource FHIR resource
     */
    default void setProfile(Resource resource) {
        resource.getMeta().setProfile(List.of(new CanonicalType(getUrl())));
    }

    /**
     * Checks if the resource has this profile
     *
     * @param resource FHIR resource
     * @return true if the resource has this profile
     */
    default boolean hasProfile(Resource resource) {
        return resource.getMeta().hasProfile(getUrl());
    }

    /**
     * Registers all profiles from an enum in the FhirContext
     *
     * @param fhirContext FhirContext
     * @param profileEnum the enum class containing profiles
     * @param <E>         the enum type
     */
    static <E extends Enum<E> & IheFhirProfile> void registerProfiles(FhirContext fhirContext, Class<E> profileEnum) {
        Arrays.stream(profileEnum.getEnumConstants())
            .filter(profile -> profile.getResourceClass() != null)
            .forEach(profile -> fhirContext.setDefaultTypeForProfile(
                profile.getUrl(),
                profile.getResourceClass()));
    }

    /**
     * Registers all profiles from an enum in the FhirContext, with additional custom types
     *
     * @param fhirContext FhirContext
     * @param profileEnum the enum class containing profiles
     * @param customTypes additional custom types to register
     * @param <E>         the enum type
     */
    @SafeVarargs
    static <E extends Enum<E> & IheFhirProfile> void registerProfiles(FhirContext fhirContext, Class<E> profileEnum,
                                                                      Class<? extends IBase>... customTypes) {
        registerProfiles(fhirContext, profileEnum);
        if (customTypes != null && customTypes.length > 0) {
            fhirContext.registerCustomTypes(Arrays.asList(customTypes));
        }
    }

    /**
     * Finds the profile enum value for a given resource
     *
     * @param resource    the FHIR resource
     * @param profileEnum the enum class containing profiles
     * @param <E>         the enum type
     * @return Optional containing the matching profile, or empty if not found
     */
    static <E extends Enum<E> & IheFhirProfile> Optional<E> profileForResource(IBaseResource resource, Class<E> profileEnum) {
        return resource.getMeta().getProfile().stream()
            .map(IPrimitiveType::getValue)
            .findFirst()
            .flatMap(url -> profileForUrl(url, profileEnum));
    }

    /**
     * Finds the profile enum value for a given URL
     *
     * @param url         the profile URL
     * @param profileEnum the enum class containing profiles
     * @param <E>         the enum type
     * @return Optional containing the matching profile, or empty if not found
     */
    static <E extends Enum<E> & IheFhirProfile> Optional<E> profileForUrl(String url, Class<E> profileEnum) {
        return Arrays.stream(profileEnum.getEnumConstants())
            .filter(p -> p.getUrl().equalsIgnoreCase(url))
            .findFirst();
    }
}