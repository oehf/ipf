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
import org.openehealth.ipf.commons.ihe.hpd.HpdUtils;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

@Slf4j
abstract public class HpdService extends AbstractWebService {

    protected BatchResponse doProcess(BatchRequest request) {
        Exchange result = process(request);
        Exception exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug(getClass().getSimpleName() + " service failed", exception);
            return errorMessage(request, exception);
        }
        return Exchanges.resultMessage(result).getBody(BatchResponse.class);
    }

    private BatchResponse errorMessage(BatchRequest batchRequest, Exception exception) {
        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setRequestID(batchRequest.getRequestID());
        batchResponse.getBatchResponses().add(HpdUtils.errorResponse(exception, batchRequest.getRequestID()));
        return batchResponse;
    }
}
