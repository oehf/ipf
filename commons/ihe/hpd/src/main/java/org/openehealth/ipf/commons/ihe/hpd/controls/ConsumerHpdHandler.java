/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.controls;

import org.openehealth.ipf.commons.ihe.hpd.controls.handlers.ConsumerHandler;
import org.openehealth.ipf.commons.ihe.hpd.controls.handlers.ConsumerHandlerBase;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse;

import javax.xml.bind.JAXBElement;

/**
 * @author Dmytro Rud
 * @since 4.3
 */
abstract public class ConsumerHpdHandler extends ConsumerHandlerBase<BatchRequest, BatchResponse> {

    public ConsumerHpdHandler(ConsumerHandler<BatchRequest, BatchResponse> wrappedHandler) {
        super(wrappedHandler);
    }

    /**
     * Combines the entries from the batch response with the ones computed locally.
     */
    protected static BatchResponse aggregateResponse(BatchRequest batchRequest, BatchResponse batchResponse, JAXBElement<?>[] localResponses) {
        batchResponse.setRequestID(batchRequest.getRequestID());
        for (int i = 0; i < localResponses.length; ++i) {
            if (localResponses[i] != null) {
                int position = Math.min(i, batchResponse.getBatchResponses().size());
                batchResponse.getBatchResponses().add(position, localResponses[i]);
            }
        }
        return batchResponse;
    }

}
