/*
 * Copyright 2024 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti119;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.r4.model.*;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;
import org.openehealth.ipf.commons.ihe.fhir.iti78.PdqPatient;

import java.util.Set;

import static org.openehealth.ipf.commons.ihe.fhir.iti119.Iti119Constants.*;

/**
 * Resource Provider for PDQm Match (ITI-119) for R4
 *
 * @author Christian Ohr
 * @since 5.0
 */
public class Iti119ResourceProvider extends AbstractPlainProvider {

    /**
     * Handles the PDQm Match request
     *
     * @param resource            patient match input
     * @param onlyCertainMatches  may be optionally set to true to indicate that the Patient Demographics Consumer
     *                            would only like matches returned when they are certain to be matches for the subject of the request
     * @param count               can be used to limit the number of results the Patient Demographics Supplier returns
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @param sortSpec            sort specification
     * @param includeSpec         include specification
     * @param requestDetails      request details
     * @return {@link IBundleProvider} instance that manages retrieving patients
     */
    @SuppressWarnings("unused")
    @Operation(name = PDQM_MATCH_OPERATION_NAME, type = PdqPatient.class)
    public IBundleProvider pdqmMatch(
        @OperationParam(name = RESOURCE, type = Patient.class) Patient resource,
        @OperationParam(name = ONLY_CERTAIN_MATCHES, type = BooleanType.class) Boolean onlyCertainMatches,
        @OperationParam(name = COUNT, type = IntegerType.class) Integer count,
        @Sort SortSpec sortSpec,
        @IncludeParam Set<Include> includeSpec,
        RequestDetails requestDetails,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse) {

        var inParams = new Parameters();
        inParams.addParameter().setResource(resource);
        inParams.addParameter().setName(ONLY_CERTAIN_MATCHES).setValue(new BooleanType(onlyCertainMatches));
        inParams.addParameter().setName(COUNT).setValue(new IntegerType(count));

        // Run down the route
        return requestBundleProvider(inParams, null, ResourceType.Patient.name(),
            httpServletRequest, httpServletResponse, requestDetails);
    }

}
