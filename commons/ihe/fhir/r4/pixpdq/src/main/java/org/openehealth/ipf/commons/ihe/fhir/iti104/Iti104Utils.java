/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti104;

import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.Constants;

import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Utilities of the Patient Identity Feed FHIR [ITI-104] transaction, shared by both ends of it.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public final class Iti104Utils {

    private Iti104Utils() {
    }

    /**
     * Determines the identifier of the patient the conditional update or delete is about. In this
     * order, it is taken from
     * <ol>
     *     <li>the {@link Iti104Parameters} the Manager has received</li>
     *     <li>the {@link Iti104Constants#ITI104_PATIENT_IDENTIFIER_HEADER} header</li>
     *     <li>the request itself, if it is an identifier (for a delete) or a Patient that has
     *     exactly one identifier (for an update)</li>
     * </ol>
     *
     * @param request    request, i.e. the Patient or the identifier of the patient to be deleted
     * @param parameters request parameters or Camel headers
     * @return the identifier of the patient, if it could be determined
     */
    public static Optional<Identifier> patientIdentifier(Object request, Map<String, Object> parameters) {
        if (parameters != null) {
            if (parameters.get(Constants.FHIR_REQUEST_PARAMETERS) instanceof Iti104Parameters iti104Parameters) {
                return Optional.of(iti104Parameters.getPatientIdentifier());
            }
            var header = toIdentifier(parameters.get(Iti104Constants.ITI104_PATIENT_IDENTIFIER_HEADER));
            if (header.isPresent()) {
                return header;
            }
        }
        if (request instanceof Patient patient) {
            return patient.getIdentifier().size() == 1 ?
                Optional.of(patient.getIdentifierFirstRep()) :
                Optional.empty();
        }
        return toIdentifier(request);
    }

    /**
     * @param identifier identifier
     * @return the identifier as FHIR token, i.e. {@code system|value}
     */
    public static String toToken(Identifier identifier) {
        return (identifier.hasSystem() ? identifier.getSystem() : "") + "|" + identifier.getValue();
    }

    private static Optional<Identifier> toIdentifier(Object o) {
        if (o instanceof Identifier identifier) {
            return Optional.of(identifier);
        }
        if (o instanceof TokenParam tokenParam) {
            return Optional.of(new Identifier()
                .setSystem(tokenParam.getSystem())
                .setValue(tokenParam.getValue()));
        }
        if (o instanceof String token && isNotBlank(token)) {
            var separator = token.indexOf('|');
            return Optional.of(separator < 0 ?
                new Identifier().setValue(token) :
                new Identifier()
                    .setSystem(token.substring(0, separator))
                    .setValue(token.substring(separator + 1)));
        }
        return Optional.empty();
    }
}
