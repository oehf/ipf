/*
 * Copyright 2015 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti83;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.SOURCE_IDENTIFIER_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.TARGET_SYSTEM_NAME;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.UriType;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;

/**
 * According to the PIXM specification, this resource provider must handle requests in the form
 * GET [base]/Patient/$ihe-pix?sourceIdentifier=[token]]{&targetSystem=[uri]}{&_format=[mime-type]}
 *
 * @author Christian Ohr
 * @since 3.6
 */
public class Iti83ResourceProvider extends AbstractPlainProvider {

    /**
     * Handles the PIXm Query
     *
     * @param resourceId            resource ID, optional
     * @param sourceIdentifierParam Identifier to search for. Should be an {@link Identifier}, but obviously
     *                              non-primitive types are forbidden in GET operations
     * @param targetSystemParamList target system URIs
     * @return {@link Parameters} containing found identifiers
     */
    @SuppressWarnings("unused")
    @Operation(name = Iti83Constants.PIXM_OPERATION_NAME, type = Patient.class, idempotent = true, returnParameters = {@OperationParam(name = "return", type = Identifier.class, max = 100)})
    public Parameters pixmQuery(
            @IdParam(optional = true) IdType resourceId,
            @OperationParam(name = SOURCE_IDENTIFIER_NAME) TokenParam sourceIdentifierParam,
            @OperationParam(name = TARGET_SYSTEM_NAME) List<UriParam> targetSystemParamList,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        var sourceIdentifier = new Identifier();

        if (resourceId == null) {
            if (sourceIdentifierParam != null) {
                sourceIdentifier.setSystem(sourceIdentifierParam.getSystem())
                        .setValue(sourceIdentifierParam.getValue());
            } else {
                throw new ForbiddenOperationException("Either resource ID or sourceIdentifier must be provided in request");
            }
        } else {
            sourceIdentifier.setValue(resourceId.getIdPart());
        }

        var inParams = new Parameters();
        inParams.addParameter().setName(SOURCE_IDENTIFIER_NAME).setValue(sourceIdentifier);

        if (targetSystemParamList != null) {
            for (var targetSystemParam : targetSystemParamList) {
                var targetUri = new UriType(targetSystemParam.getValue());
                inParams.addParameter().setName(TARGET_SYSTEM_NAME).setValue(targetUri);
            }
        }
        // Run down the route
        return requestResource(inParams, null, Parameters.class,
                httpServletRequest, httpServletResponse, requestDetails);
    }
}
