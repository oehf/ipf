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
    String FHIR_REQUEST_DETAILS = "FhirRequestDetails";

    // Parameter information from the HttpServletRequest
    String HTTP_AUTHORIZATION = "Authorization";
    String HTTP_URI = "FhirHttpUri";
    String HTTP_URL = "FhirHttpUrl";
    String HTTP_SCHEME = "FhirHttpScheme";
    String HTTP_METHOD = "FhirHttpMethod";
    String HTTP_QUERY = "FhirHttpQuery";
    String HTTP_CHARACTER_ENCODING = "FhirHttpCharacterEncoding";
    String HTTP_CONTENT_TYPE = "FhirHttpContentType";
    String HTTP_PROTOCOL_VERSION = "FhirHttpProtocolVersion";
    String HTTP_CLIENT_IP_ADDRESS = "FhirHttpClientIpAddress";
    String HTTP_LOCALES = "FhirHttpAcceptLanguage";
    String HTTP_USER = "FhirHttpUserPrincipal";
    String HTTP_INCOMING_HEADERS = "FhirHttpHeaders";
    String HTTP_OUTGOING_HEADERS = "FhirHttpOutgoingHeaders";
    /**
     * @deprecated use {@link #HTTP_INCOMING_HEADERS} instead
     */
    @Deprecated
    String HTTP_HEADERS = HTTP_INCOMING_HEADERS;
    String HTTP_X509_CERTIFICATES = "FhirHttpCertificates";

    String FHIR_RESOURCE_TYPE_HEADER = "RESOURCE_TYPE_HEADER";
    String FHIR_OPERATION_HEADER = "OPERATION_HEADER";
    String FHIR_AUDIT_HEADER = "AUDIT_HEADER";

    // Paging stuff
    String FHIR_COUNT = ca.uhn.fhir.rest.api.Constants.PARAM_COUNT;
    String FHIR_FIRST = ca.uhn.fhir.rest.api.Constants.LINK_FIRST;
    String FHIR_LAST= ca.uhn.fhir.rest.api.Constants.LINK_LAST;
    String FHIR_PREVIOUS = ca.uhn.fhir.rest.api.Constants.LINK_PREVIOUS;
    String FHIR_NEXT = ca.uhn.fhir.rest.api.Constants.LINK_NEXT;

    String FHIR_FROM_INDEX = "FhirFromIndex";
    String FHIR_TO_INDEX = "FhirToIndex";
    String FHIR_REQUEST_SIZE_ONLY = "FhirRequestSizeOnly";
    String FHIR_REQUEST_GET_ONLY = "FhirRequestGetOnly";

    String URN_IETF_RFC_3986 = "urn:ietf:rfc:3986";
}
