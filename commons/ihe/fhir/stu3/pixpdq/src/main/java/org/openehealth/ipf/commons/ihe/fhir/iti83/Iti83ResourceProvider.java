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

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.UriType;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.SOURCE_IDENTIFIER_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.TARGET_SYSTEM_NAME;

/**
 * According to the PIXM specification, this resource provider must handle requests in the form
 * GET [base]/Patient/$ihe-pix?sourceIdentifier=[token]]{&targetSystem=[uri]}{&_format=[mime-type]}
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class Iti83ResourceProvider extends AbstractPlainProvider {

    /**
     * Handles the PIXm Query
     *
     * @param sourceIdentifierParam Identifier to search for. Should be an {@link Identifier}, but obviously
     *                              non-primitive types are forbidden in GET operations
     * @param targetSystemParam     target system URI
     * @return {@link Parameters} containing found identifiers
     */
    @SuppressWarnings("unused")
    @Operation(name = Iti83Constants.PIXM_OPERATION_NAME, type = Patient.class, idempotent = true, returnParameters = {@OperationParam(name = "return", type = Identifier.class, max = 100)})
    public Parameters pixmQuery(
            @OperationParam(name = SOURCE_IDENTIFIER_NAME) TokenParam sourceIdentifierParam,
            @OperationParam(name = TARGET_SYSTEM_NAME) UriParam targetSystemParam,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        Identifier sourceIdentifier = new Identifier()
                .setSystem(sourceIdentifierParam.getSystem())
                .setValue(sourceIdentifierParam.getValue());
        UriType targetUri = targetSystemParam == null ? null : new UriType(targetSystemParam.getValue());

        Parameters inParams = new Parameters();
        inParams.addParameter().setName(SOURCE_IDENTIFIER_NAME).setValue(sourceIdentifier);
        inParams.addParameter().setName(TARGET_SYSTEM_NAME).setValue(targetUri);

        // Run down the route
        return requestResource(inParams, Parameters.class, httpServletRequest, httpServletResponse);
    }

    /**
     * Handles the PIXm Retrieve. Note that this is not part of the specification, but a useful variant that allows to use resource IDs
     * in requests such as .../Patient/4711/$ihe-pix would be equivalent with .../Patient/$ihe-pix?sourceIdentifier=URI|4711 where URI
     * identifies the NamingSystem used for resource IDs
     *
     * @param resourceId          resource ID
     * @param targetSystemParam   target system URI
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @return {@link Parameters} containing found identifiers
     */
    @Operation(name = Iti83Constants.PIXM_OPERATION_NAME, type = Patient.class, idempotent = true, returnParameters = {@OperationParam(name = "return", type = Identifier.class, max = 100)})
    public Parameters pixmRetrieve(
            @IdParam IdType resourceId,
            @OperationParam(name = TARGET_SYSTEM_NAME) UriParam targetSystemParam,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        if (resourceId == null) throw new InvalidRequestException("Must provide ID with READ request");
        UriType targetUri = targetSystemParam == null ? null : new UriType(targetSystemParam.getValue());

        Parameters inParams = new Parameters();
        inParams.addParameter().setName(SOURCE_IDENTIFIER_NAME).setValue(new Identifier().setValue(resourceId.getIdPart()));
        inParams.addParameter().setName(TARGET_SYSTEM_NAME).setValue(targetUri);

        // Run down the route
        return requestResource(inParams, Parameters.class, httpServletRequest, httpServletResponse);
    }
}
