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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti83;

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import org.hl7.fhir.instance.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.fhir.atna.iti83.Iti83AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.iti83.PixmRequest;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.AbstractResourceProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.*;

/**
 * According to the PIXM specification, this resource provider must handle requests in the form
 * GET [base]/Patient/$ihe-pix?sourceIdentifier=[token]]{&targetSystem=[uri]}{&_format=[mime-type]}
 */
public class Iti83ResourceProvider extends AbstractResourceProvider<Iti83AuditDataset> {

    /**
     * Signals that $pix-query operations are directed against the Patient resource type
     *
     * @return Patient.class
     */
    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Patient.class;
    }

    /**
     * Handles the PIXm Query
     *
     * @param sourceIdentifierString Identifier to search for. Should be an {@link Identifier}, but obviously
     *                               non-primitive types are forbidden in GET operations
     * @param targetSystem           target system URI
     * @return {@link Parameters} containing found identifiers
     */
    @SuppressWarnings("unused")
    @Operation(name = PIXM_OPERATION_NAME, idempotent = true)
    public Parameters pixmQuery(
            @OperationParam(name = SOURCE_IDENTIFIER_NAME) StringType sourceIdentifierString,
            @OperationParam(name = TARGET_SYSTEM_NAME) UriType targetSystem,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        // Split up the primitive String into an {@link Identifier}
        String[] parts = sourceIdentifierString.getValueAsString().split("\\|");
        Identifier sourceIdentifier = new Identifier()
                .setSystem(parts[0])
                .setValue(parts[1]);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SOURCE_IDENTIFIER_NAME, sourceIdentifier);
        parameters.put(TARGET_SYSTEM_NAME, targetSystem);

        // Run down the route
        return processInRoute(null, parameters, Parameters.class, httpServletRequest, httpServletResponse);
    }

}
