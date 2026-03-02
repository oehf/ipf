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
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Pixm310;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PixmProfile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Parameters resource for ITI-83 Mobile Patient Identifier Cross-reference Query input as defined by the PIXm specification.
 * <p>
 * This Parameters resource contains the input parameters for the $ihe-pix operation, including the source identifier
 * (in "system|value" format) and optional target system(s) for cross-referencing.
 * </p>
 *
 * @author Christian Ohr
 * @since 5.2
 */
@ResourceDef(name = "Parameters", profile = PixmProfile.PIXM_QUERY_PARAMETERS_IN_PROFILE)
public class PixmQueryParametersIn extends Parameters implements Pixm310 {

    public static final String SOURCE_IDENTIFIER = "sourceIdentifier";
    public static final String TARGET_SYSTEM = "targetSystem";
    public static final String _FORMAT = "_format";

    public PixmQueryParametersIn() {
        super();
        PixmProfile.PIXM_QUERY_PARAMETERS_IN.setProfile(this);
    }

    @Override
    public PixmQueryParametersIn copy() {
        var copy = new PixmQueryParametersIn();
        copyValues(copy);
        return copy;
    }

    /**
     * Gets the source identifier parameter value as a string in "system|value" format.
     *
     * @return the source identifier string, or null if not present
     */
    public String getSourceIdentifier() {
        return getParameter().stream()
            .filter(p -> SOURCE_IDENTIFIER.equals(p.getName()))
            .map(ParametersParameterComponent::getValue)
            .filter(v -> v instanceof StringType)
            .map(v -> ((StringType) v).getValue())
            .findFirst()
            .orElse(null);
    }

    /**
     * Gets the source identifier as an Identifier object by parsing the "system|value" format.
     *
     * @return the parsed Identifier, or null if not present or invalid format
     */
    public Identifier getSourceIdentifierAsIdentifier() {
        String sourceIdentifier = getSourceIdentifier();
        if (sourceIdentifier == null) {
            return null;
        }
        
        int pipeIndex = sourceIdentifier.indexOf('|');
        if (pipeIndex == -1) {
            // No pipe separator, treat entire string as value with no system
            return new Identifier().setValue(sourceIdentifier);
        }
        
        String system = sourceIdentifier.substring(0, pipeIndex);
        String value = sourceIdentifier.substring(pipeIndex + 1);
        
        Identifier identifier = new Identifier();
        if (!system.isEmpty()) {
            identifier.setSystem(system);
        }
        if (!value.isEmpty()) {
            identifier.setValue(value);
        }
        return identifier;
    }

    /**
     * Sets the source identifier parameter from a string in "system|value" format.
     *
     * @param sourceIdentifier the source identifier string
     * @return this Parameters instance
     */
    public PixmQueryParametersIn setSourceIdentifier(String sourceIdentifier) {
        getParameter().removeIf(p -> SOURCE_IDENTIFIER.equals(p.getName()));
        if (sourceIdentifier != null) {
            addParameter()
                .setName(SOURCE_IDENTIFIER)
                .setValue(new StringType(sourceIdentifier));
        }
        return this;
    }

    /**
     * Sets the source identifier parameter from an Identifier object, converting it to "system|value" format.
     *
     * @param sourceIdentifier the source identifier
     * @return this Parameters instance
     */
    public PixmQueryParametersIn setSourceIdentifier(Identifier sourceIdentifier) {
        if (sourceIdentifier == null) {
            return setSourceIdentifier((String) null);
        }
        
        String system = sourceIdentifier.getSystem() != null ? sourceIdentifier.getSystem() : "";
        String value = sourceIdentifier.getValue() != null ? sourceIdentifier.getValue() : "";
        String identifierString = system + "|" + value;
        
        return setSourceIdentifier(identifierString);
    }

    /**
     * Gets the target system parameter values.
     *
     * @return list of target system URIs
     */
    public List<String> getTargetSystems() {
        return getParameter().stream()
            .filter(p -> TARGET_SYSTEM.equals(p.getName()))
            .map(ParametersParameterComponent::getValue)
            .filter(v -> v instanceof UriType)
            .map(v -> ((UriType) v).getValue())
            .collect(Collectors.toList());
    }

    /**
     * Adds a target system parameter.
     *
     * @param targetSystem the target system URI
     * @return this Parameters instance
     */
    public PixmQueryParametersIn addTargetSystem(String targetSystem) {
        if (targetSystem != null) {
            addParameter()
                .setName(TARGET_SYSTEM)
                .setValue(new UriType(targetSystem));
        }
        return this;
    }

    /**
     * Sets the target system parameters, replacing any existing ones.
     *
     * @param targetSystems list of target system URIs
     * @return this Parameters instance
     */
    public PixmQueryParametersIn setTargetSystems(List<String> targetSystems) {
        getParameter().removeIf(p -> TARGET_SYSTEM.equals(p.getName()));
        if (targetSystems != null) {
            targetSystems.forEach(this::addTargetSystem);
        }
        return this;
    }

    /**
     * Gets the _format parameter value.
     *
     * @return the format string, or null if not present
     */
    public String getFormat() {
        return getParameter().stream()
            .filter(p -> _FORMAT.equals(p.getName()))
            .map(ParametersParameterComponent::getValue)
            .filter(v -> v instanceof StringType)
            .map(v -> ((StringType) v).getValue())
            .findFirst()
            .orElse(null);
    }

    /**
     * Sets the _format parameter.
     *
     * @param format the format string
     * @return this Parameters instance
     */
    public PixmQueryParametersIn setFormat(String format) {
        getParameter().removeIf(p -> _FORMAT.equals(p.getName()));
        if (format != null) {
            addParameter()
                .setName(_FORMAT)
                .setValue(new StringType(format));
        }
        return this;
    }
}