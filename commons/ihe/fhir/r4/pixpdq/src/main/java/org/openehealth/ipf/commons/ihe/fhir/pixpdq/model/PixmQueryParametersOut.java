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
package org.openehealth.ipf.commons.ihe.fhir.pixpdq.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Pixm310;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PixmProfile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Parameters resource for ITI-83 Mobile Patient Identifier Cross-reference Query output as defined by the PIXm specification.
 * <p>
 * This Parameters resource contains the output parameters from the $ihe-pix operation, including the target identifiers
 * that correspond to the source identifier provided in the query.
 * </p>
 *
 * @author Christian Ohr
 * @since 5.2
 */
@ResourceDef(name = "Parameters", profile = PixmProfile.PIXM_QUERY_PARAMETERS_OUT_PROFILE)
public class PixmQueryParametersOut extends Parameters implements Pixm310 {

    public static final String TARGET_IDENTIFIER = "targetIdentifier";
    public static final String TARGET_ID = "targetId";

    public PixmQueryParametersOut() {
        super();
        PixmProfile.PIXM_QUERY_PARAMETERS_OUT.setProfile(this);
    }

    @Override
    public PixmQueryParametersOut copy() {
        var copy = new PixmQueryParametersOut();
        copyValues(copy);
        return copy;
    }

    /**
     * Gets the target identifier parameter values.
     *
     * @return list of target identifiers
     */
    public List<Identifier> getTargetIdentifiers() {
        return getParameter().stream()
            .filter(p -> TARGET_IDENTIFIER.equals(p.getName()))
            .map(ParametersParameterComponent::getValue)
            .filter(v -> v instanceof Identifier)
            .map(v -> (Identifier) v)
            .collect(Collectors.toList());
    }

    /**
     * Adds a target identifier parameter.
     *
     * @param targetIdentifier the target identifier
     * @return this Parameters instance
     */
    public PixmQueryParametersOut addTargetIdentifier(Identifier targetIdentifier) {
        if (targetIdentifier != null) {
            addParameter()
                .setName(TARGET_IDENTIFIER)
                .setValue(targetIdentifier);
        }
        return this;
    }

    /**
     * Sets the target identifier parameters, replacing any existing ones.
     *
     * @param targetIdentifiers list of target identifiers
     * @return this Parameters instance
     */
    public PixmQueryParametersOut setTargetIdentifiers(List<Identifier> targetIdentifiers) {
        getParameter().removeIf(p -> TARGET_IDENTIFIER.equals(p.getName()));
        if (targetIdentifiers != null) {
            targetIdentifiers.forEach(this::addTargetIdentifier);
        }
        return this;
    }

    /**
     * Gets the target ID parameter values (references to Patient resources).
     *
     * @return list of target ID references
     */
    public List<Reference> getTargetIds() {
        return getParameter().stream()
            .filter(p -> TARGET_ID.equals(p.getName()))
            .map(ParametersParameterComponent::getValue)
            .filter(v -> v instanceof Reference)
            .map(v -> (Reference) v)
            .collect(Collectors.toList());
    }

    /**
     * Adds a target ID parameter.
     *
     * @param targetId the target ID reference
     * @return this Parameters instance
     */
    public PixmQueryParametersOut addTargetId(Reference targetId) {
        if (targetId != null) {
            addParameter()
                .setName(TARGET_ID)
                .setValue(targetId);
        }
        return this;
    }

    /**
     * Sets the target ID parameters, replacing any existing ones.
     *
     * @param targetIds list of target ID references
     * @return this Parameters instance
     */
    public PixmQueryParametersOut setTargetIds(List<Reference> targetIds) {
        getParameter().removeIf(p -> TARGET_ID.equals(p.getName()));
        if (targetIds != null) {
            targetIds.forEach(this::addTargetId);
        }
        return this;
    }
}