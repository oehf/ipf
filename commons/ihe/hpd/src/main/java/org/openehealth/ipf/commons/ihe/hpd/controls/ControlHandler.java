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

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse;

import javax.xml.bind.JAXBElement;

/**
 * Abstract server-side handler of LDAP controls.
 *
 * @author Dmytro Rud
 * @since 3.7.5
 */
abstract public class ControlHandler implements Handler<BatchRequest, BatchResponse> {

    @Getter
    private final Handler<BatchRequest, BatchResponse> wrappedHandler;

    public ControlHandler(Handler<BatchRequest, BatchResponse> wrappedHandler) {
        this.wrappedHandler = wrappedHandler;
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
