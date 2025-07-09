/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.svs.iti48;

import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.Action;

/**
 * Provides the ITI-48 web-service interface (SOAP binding).
 */
@WebService(targetNamespace = "urn:ihe:iti:svs:2008", name = "ValueSetRepository_PortType", portName = "ValueSetRepository_Port_Soap12")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Iti48PortType {

    /**
     * Performs a stored query according to the ITI-48 specification.
     *
     * @param body The query request.
     * @return the query response.
     */
    @WebResult(name = "RetrieveValueSetResponse",
        targetNamespace = "urn:ihe:iti:svs:2008",
        partName = "body")
    @Action(input = "urn:ihe:iti:2008:RetrieveValueSet",
        output = "urn:ihe:iti:2008:RetrieveValueSetResponse")
    @WebMethod(operationName = "ValueSetRepository_RetrieveValueSet")
    RetrieveValueSetResponse valueSetRepositoryRetrieveValueSet(
        @WebParam(partName = "body",
            name = "RetrieveValueSetRequest",
            targetNamespace = "urn:ihe:iti:svs:2008")
        final RetrieveValueSetRequest body
    );
}