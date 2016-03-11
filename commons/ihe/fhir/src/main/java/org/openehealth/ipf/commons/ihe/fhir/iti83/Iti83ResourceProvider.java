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

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import org.hl7.fhir.instance.model.*;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.*;

/**
 * According to the PIXM specification, this resource provider must handle requests in the form
 * GET [base]/Patient/$ihe-pix?sourceIdentifier=[token]]{&targetSystem=[uri]}{&_format=[mime-type]}
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class Iti83ResourceProvider extends AbstractPlainProvider {

    /**
     * Handles the PIXm Query
     *
     * @param sourceIdentifierString Identifier to search for. Should be an {@link Identifier}, but obviously
     *                               non-primitive types are forbidden in GET operations
     * @param targetSystem           target system URI
     * @return {@link Parameters} containing found identifiers
     */
    @SuppressWarnings("unused")
    @Operation(name = PIXM_OPERATION_NAME, type = Patient.class, idempotent = true, returnParameters = { @OperationParam(name = "return", type = Identifier.class, max = 100)})
    public Parameters pixmQuery(
            @OperationParam(name = SOURCE_IDENTIFIER_NAME) StringType sourceIdentifierString,
            @OperationParam(name = TARGET_SYSTEM_NAME) UriType targetSystem,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        // Split up the primitive String into an {@link Identifier}
        String[] parts = sourceIdentifierString.getValueAsString().split("\\|");
        Identifier sourceIdentifier = new Identifier()
                .setSystem(parts.length > 0 ? parts[0] : null)
                .setValue(parts.length > 1 ? parts[1] : null);

        Parameters inParams = new Parameters();
        inParams.addParameter().setName(SOURCE_IDENTIFIER_NAME).setValue(sourceIdentifier);
        inParams.addParameter().setName(TARGET_SYSTEM_NAME).setValue(targetSystem);

        // Run down the route
        return requestResource(inParams, Parameters.class, httpServletRequest, httpServletResponse);
    }

}
