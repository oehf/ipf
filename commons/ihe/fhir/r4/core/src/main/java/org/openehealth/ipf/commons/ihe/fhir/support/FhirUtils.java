/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Common utilities for handling FHIR resources
 *
 * @author Christian Ohr
 * @since 3.6
 */
public final class FhirUtils {


    /**
     * Converts a {@link Bundle} into a map grouped by the entry resources types
     *
     * @param bundle Bundle
     * @return map of entries grouped by their resource type
     */
    public static Map<ResourceType, List<Bundle.BundleEntryComponent>> getBundleEntries(Bundle bundle) {
        return bundle.getEntry().stream()
                .collect(Collectors.groupingBy(entry -> {
                            var request = entry.getRequest();
                            if (request == null || request.getUrl() == null) {
                                throw unprocessableEntity(
                                        OperationOutcome.IssueSeverity.ERROR,
                                        OperationOutcome.IssueType.INVALID,
                                        null, null,
                                        "Invalid bundle entry request element %s",
                                        entry);
                            }
                            return entry.getResource().getResourceType();
                        }
                , LinkedHashMap::new, Collectors.toList()));
    }

    /**
     * @see ca.uhn.fhir.util.BundleUtil#toListOfResourcesOfType(FhirContext, IBaseBundle, Class)
     */
    public static <T extends Resource>  List<T> getResources(Bundle bundle, Class<T> clazz) {
        return bundle.getEntry().stream()
            .map(Bundle.BundleEntryComponent::getResource)
            .filter(c -> clazz.isAssignableFrom(c.getClass()))
            .map(clazz::cast)
            .collect(Collectors.toList());
    }

    public static <T extends Resource> Optional<T> getOptionalResource(Bundle bundle, Class<T> clazz) {
        return bundle.getEntry().stream()
            .map(Bundle.BundleEntryComponent::getResource)
            .filter(c -> clazz.isAssignableFrom(c.getClass()))
            .map(clazz::cast)
            .findFirst();
    }

    public static <T extends Resource> T getResource(Bundle bundle, Class<T> clazz) {
        return getOptionalResource(bundle, clazz)
            .orElseThrow(() -> new RuntimeException("Expected entry with resource of type " + clazz.getName()));
    }

    // Generate Exceptions

    public static InternalErrorException internalError(
            OperationOutcome.IssueSeverity severity,
            OperationOutcome.IssueType type,
            String code,
            String diagnostics,
            String msg,
            Object... args) {
        return exception(InternalErrorException::new, severity, type, code, diagnostics, msg, args);
    }

    public static UnprocessableEntityException unprocessableEntity(
            OperationOutcome.IssueSeverity severity,
            OperationOutcome.IssueType type,
            String code,
            String diagnostics,
            String msg,
            Object... args) {
        return exception(UnprocessableEntityException::new, severity, type, code, diagnostics, msg, args);
    }

    public static InvalidRequestException invalidRequest(
            OperationOutcome.IssueSeverity severity,
            OperationOutcome.IssueType type,
            String code,
            String diagnostics,
            String msg,
            Object... args) {
        return exception(InvalidRequestException::new, severity, type, code, diagnostics, msg, args);
    }

    public static ResourceNotFoundException resourceNotFound(
            OperationOutcome.IssueSeverity severity,
            OperationOutcome.IssueType type,
            String code,
            String diagnostics,
            String msg,
            Object... args) {
        return exception(ResourceNotFoundException::new, severity, type, code, diagnostics, msg, args);
    }

    public static ForbiddenOperationException forbiddenOperation(
            OperationOutcome.IssueSeverity severity,
            OperationOutcome.IssueType type,
            String code,
            String diagnostics,
            String msg,
            Object... args) {
        return exception(ForbiddenOperationException::new, severity, type, code, diagnostics, msg, args);
    }

    public static <T extends BaseServerResponseException> T exception(Function<String, T> func,
                                                                      OperationOutcome.IssueSeverity severity,
                                                                      OperationOutcome.IssueType type,
                                                                      String code,
                                                                      String diagnostics,
                                                                      String msg,
                                                                      Object... args) {
        var operationOutcome = new OperationOutcome();
        CodeableConcept errorCode = null;
        if (code != null) {
            errorCode = new CodeableConcept();
            errorCode.addCoding().setCode(code);
        }
        operationOutcome.addIssue()
                .setSeverity(severity)
                .setCode(type)
                .setDetails(errorCode)
                .setDiagnostics(diagnostics);
        return exception(func, operationOutcome, msg, args);
    }

    public static <T extends BaseServerResponseException> T exception(Function<String, T> func,
                                                                      IBaseOperationOutcome operationOutcome,
                                                                      String msg,
                                                                      Object... args) {
        var exception = func.apply(String.format(msg, args));
        exception.setOperationOutcome(operationOutcome);
        return exception;
    }

    public static void logValidationMessage(Logger log, OperationOutcome.OperationOutcomeIssueComponent issue) {
        Level level = switch (issue.getSeverity()) {
            case ERROR, FATAL -> Level.ERROR;
            case WARNING -> Level.WARN;
            case INFORMATION -> Level.INFO;
            default -> Level.DEBUG;
        };
        log.atLevel(level).log("Validation issue: {} : {}", issue.getSeverity().getDisplay(), issue.getDiagnostics());
    }
}
