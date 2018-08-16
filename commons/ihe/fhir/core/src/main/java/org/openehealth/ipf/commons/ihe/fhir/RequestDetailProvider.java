/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Track the {@link RequestDetails} and make it available to downstream processing. This
 * needs to be thread local, because the request details are normally not forwarded to
 * the resource provider methods.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class RequestDetailProvider extends InterceptorAdapter {

    private static final ThreadLocal<RequestDetails> requestDetails = new ThreadLocal<>();

    public static RequestDetails getRequestDetails() {
        return requestDetails.get();
    }

    @Override
    public void incomingRequestPreHandled(RestOperationTypeEnum operation, ActionRequestDetails processedRequest) {
        requestDetails.set(processedRequest.getRequestDetails());
    }

    @Override
    public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
        requestDetails.set(null);
    }

    @Override
    public boolean handleException(RequestDetails theRequestDetails,
                                   BaseServerResponseException theException,
                                   HttpServletRequest theServletRequest,
                                   HttpServletResponse theServletResponse) {
        requestDetails.set(null);
        return true;
    }

}
