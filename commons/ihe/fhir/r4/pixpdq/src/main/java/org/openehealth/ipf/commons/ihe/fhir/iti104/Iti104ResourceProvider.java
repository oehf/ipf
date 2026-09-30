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
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.r4.model.IdType;

/**
 * Resource Provider for the Patient Identity Feed FHIR [ITI-104] transaction of a Patient Identifier
 * Cross-reference Manager that supports the Remove Patient Option. In addition to the conditional
 * update of {@link Iti104UpdateResourceProvider}, it accepts the conditional delete
 * ({@code DELETE Patient?identifier=system|value}) of the Remove Patient message. The route then
 * receives the {@link org.hl7.fhir.r4.model.Identifier} of the patient as body.
 * <p>
 * This is the default provider of the transaction.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class Iti104ResourceProvider extends Iti104UpdateResourceProvider {

    @Delete
    public MethodOutcome deletePatient(
        @IdParam IdType resourceId,
        @ConditionalUrlParam String conditionalUrl,
        RequestDetails requestDetails,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse) {
        var parameters = iti104Parameters(resourceId, conditionalUrl, requestDetails);
        return requestAction(parameters.getPatientIdentifier(), parameters, httpServletRequest, httpServletResponse, requestDetails);
    }
}
