/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.NotImplementedException;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ErrorResponse;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.LDAPResult;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ObjectFactory;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.SearchResponse;

import jakarta.xml.bind.JAXBElement;

/**
 * @author Dmytro Rud
 * @since 4.3
 */
@UtilityClass
public class HpdUtils {

    public static final ObjectFactory DSMLV2_OBJECT_FACTORY = new ObjectFactory();

    public static JAXBElement<ErrorResponse> errorResponse(Exception exception, String requestId) {
        ErrorResponse error = DSMLV2_OBJECT_FACTORY.createErrorResponse();
        error.setMessage(exception.getMessage());
        error.setRequestID(requestId);
        ErrorResponse.ErrorType errorType = (exception instanceof HpdException) ? ((HpdException) exception).getType() : ErrorResponse.ErrorType.OTHER;
        error.setType(errorType);
        return DSMLV2_OBJECT_FACTORY.createBatchResponseErrorResponse(error);
    }

    public static String extractResponseRequestId(Object dsmlResponse) {
        if (dsmlResponse instanceof SearchResponse) {
            return ((SearchResponse) dsmlResponse).getRequestID();
        } else if (dsmlResponse instanceof LDAPResult) {
            return ((LDAPResult) dsmlResponse).getRequestID();
        } else if (dsmlResponse instanceof ErrorResponse) {
            return ((ErrorResponse) dsmlResponse).getRequestID();
        } else {
            throw new NotImplementedException("Cannot handle HPD response type " + dsmlResponse.getClass() + ", please submit a bug report");
        }
    }

}
