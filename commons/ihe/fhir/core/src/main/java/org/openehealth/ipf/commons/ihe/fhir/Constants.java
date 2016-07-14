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

package org.openehealth.ipf.commons.ihe.fhir;

/**
 * @author Christian Ohr
 * @since 3.1
 */
public interface Constants {

    String SOURCE_IDENTIFIER_NAME = "sourceIdentifier";
    String TARGET_SYSTEM_NAME = "targetSystem";

    String FHIR_CONTEXT = "FhirContext";

    // Request parameter information
    String FHIR_REQUEST_PARAMETERS = "FhirRequestParameters";


    // Parameter information from the HttpServletRequest
    String HTTP_URI = "FhirHttpUri";
    String HTTP_URL = "FhirHttpUrl";
    String HTTP_METHOD = "FhirHttpMethod";
    String HTTP_QUERY = "FhirHttpQuery";
    String HTTP_CHARACTER_ENCODING = "FhirHttpCharacterEncoding";
    String HTTP_CONTENT_TYPE = "FhirHttpContentType";
    String HTTP_PROTOCOL_VERSION = "FhirHttpProtocolVersion";
    String HTTP_CLIENT_IP_ADDRESS = "FhirHttpClientIpAddress";
    String HTTP_HEADERS = "FhirHttpHeaders";


    // Paging stuff
    String FHIR_COUNT = ca.uhn.fhir.rest.server.Constants.PARAM_COUNT;
    String FHIR_FIRST = ca.uhn.fhir.rest.server.Constants.LINK_FIRST;
    String FHIR_LAST= ca.uhn.fhir.rest.server.Constants.LINK_LAST;
    String FHIR_PREVIOUS = ca.uhn.fhir.rest.server.Constants.LINK_PREVIOUS;
    String FHIR_NEXT = ca.uhn.fhir.rest.server.Constants.LINK_NEXT;

    String FHIR_FROM_INDEX = "FhirFromIndex";
    String FHIR_TO_INDEX = "FhirToIndex";
    String FHIR_REQUEST_SIZE_ONLY = "FhirRequestSizeOnly";

    String URN_IETF_RFC_3986 = "urn:ietf:rfc:3986";
}
