/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hpd;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.hpd.HpdException;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ErrorResponse;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ObjectFactory;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ErrorResponse.ErrorType;

@Slf4j
abstract public class HpdService extends AbstractWebService {

    public BatchResponse doProcess(BatchRequest request) {
        Exchange result = process(request);
        Exception exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug(getClass().getSimpleName() + " service failed", exception);
            return errorMessage(request, exception);
        }
        return Exchanges.resultMessage(result).getBody(BatchResponse.class);
    }

    private BatchResponse errorMessage(BatchRequest request, Exception exception) {
        ObjectFactory factory = new ObjectFactory();

        ErrorResponse error = factory.createErrorResponse();
        error.setMessage(exception.getMessage());
        error.setRequestID(request.getRequestID());
        ErrorType errorType = (exception instanceof HpdException) ? ((HpdException) exception).getType() : ErrorType.OTHER;
        error.setType(errorType);

        BatchResponse response = factory.createBatchResponse();
        response.setRequestID(request.getRequestID());
        response.getBatchResponses().add(factory.createBatchResponseErrorResponse(error));
        
        return response;
    }
}
