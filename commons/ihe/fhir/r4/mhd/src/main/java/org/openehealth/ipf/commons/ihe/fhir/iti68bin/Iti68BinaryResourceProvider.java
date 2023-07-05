/*
 * Copyright 2016 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti68bin;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.IdType;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Resource Provider for MHD (ITI-68) based on reading a Binary resource
 *
 * @author Christian Ohr
 * @since 3.7
 */
public class Iti68BinaryResourceProvider extends AbstractPlainProvider {

    @SuppressWarnings("unused")
    @Read(type = Binary.class)
    public Binary getBinary(
            @IdParam IdType id,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        return requestResource(id, null, Binary.class,
                httpServletRequest, httpServletResponse, requestDetails);
    }

}
