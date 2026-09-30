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

import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.AbstractResourceProvider;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openehealth.ipf.commons.ihe.fhir.iti104.Iti104Constants.IDENTIFIER_PARAMETER;

/**
 * Resource Provider for the Patient Identity Feed FHIR [ITI-104] transaction of a Patient Identifier
 * Cross-reference Manager that does not support the Remove Patient Option. It only accepts the
 * conditional update ({@code PUT Patient?identifier=system|value}) of the Add/Revise Patient and
 * Resolve Duplicate Patient messages, so delete is not advertised in the CapabilityStatement. This
 * suits e.g. a Manager that proxies ITI-104 to a PIX Feed (ITI-8), which has no equivalent of
 * Remove Patient. Use it with the {@code resourceProvider} endpoint parameter.
 * <p>
 * Requests that are not conditional on exactly one patient identifier are rejected. The route receives
 * the Patient as body, and the identifier as {@link Iti104Parameters} in the
 * {@link org.openehealth.ipf.commons.ihe.fhir.Constants#FHIR_REQUEST_PARAMETERS} header. It is
 * expected to return a {@link MethodOutcome}; for an update that created the patient,
 * {@link MethodOutcome#setCreated(Boolean)} shall be set.
 * <p>
 * ITI-104 defers to the FHIR conditional update and delete semantics, which the route has to implement,
 * as only it knows the patients: e.g. if the condition matches more than one patient, it shall reject the
 * request with 412 Precondition Failed ({@link ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException}).
 * A conditional delete that matches no patient is not an error.
 *
 * @author Christian Ohr
 * @since 6.0
 * @see Iti104ResourceProvider
 */
public class Iti104UpdateResourceProvider extends AbstractResourceProvider {

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Patient.class;
    }

    @Update
    public MethodOutcome updatePatient(
        @ResourceParam Patient patient,
        @IdParam IdType resourceId,
        @ConditionalUrlParam String conditionalUrl,
        RequestDetails requestDetails,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse) {
        var parameters = iti104Parameters(resourceId, conditionalUrl, requestDetails);
        return requestAction(patient, parameters, httpServletRequest, httpServletResponse, requestDetails);
    }

    protected static Iti104Parameters iti104Parameters(IdType resourceId, String conditionalUrl, RequestDetails requestDetails) {
        if (conditionalUrl == null || (resourceId != null && resourceId.hasIdPart())) {
            throw new InvalidRequestException("ITI-104 requires a conditional request on the patient identifier, e.g. Patient?"
                + IDENTIFIER_PARAMETER + "=system|value");
        }
        var parameters = requestDetails.getParameters();
        // parameters like _format do not belong to the condition
        var otherConditions = parameters.keySet().stream()
            .anyMatch(name -> !name.startsWith("_") && !IDENTIFIER_PARAMETER.equals(name));
        if (otherConditions || !parameters.containsKey(IDENTIFIER_PARAMETER) || parameters.get(IDENTIFIER_PARAMETER).length != 1) {
            throw new InvalidRequestException("ITI-104 requires exactly one '" + IDENTIFIER_PARAMETER
                + "' parameter as condition, but got " + conditionalUrl);
        }
        var token = parameters.get(IDENTIFIER_PARAMETER)[0];
        var separator = token.indexOf('|');
        if (separator <= 0 || !isNotBlank(token.substring(separator + 1))) {
            throw new InvalidRequestException("ITI-104 requires the patient identifier as system|value, but got " + token);
        }
        var identifier = new TokenParam(token.substring(0, separator), token.substring(separator + 1));
        return new Iti104Parameters(identifier, requestDetails.getFhirContext());
    }
}
