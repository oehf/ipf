/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.stub.json

import groovy.transform.CompileStatic
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*

/**
 * @author Dmytro Rud
 */
@CompileStatic
class SearchBatchRequestIntermediary {

    String requestId
    AuthRequest authRequest
    List<SearchRequestIntermediary> searchRequests
    BatchRequest.RequestProcessingType processing
    BatchRequest.RequestResponseOrder responseOrder
    BatchRequest.RequestErrorHandlingType onError

    static SearchBatchRequestIntermediary fromBatchRequest(BatchRequest batchRequest) {
        return new SearchBatchRequestIntermediary(
            requestId:      batchRequest.requestID,
            authRequest:    batchRequest.authRequest,
            searchRequests: batchRequest.batchRequests.collect { SearchRequestIntermediary.fromSearchRequest(it as SearchRequest) },
            processing:     batchRequest.processing,
            responseOrder:  batchRequest.responseOrder,
            onError:        batchRequest.onError,
        )
    }

    BatchRequest toBatchRequest() {
        return new BatchRequest(
            requestID:      requestId,
            authRequest:    authRequest,
            batchRequests:  searchRequests.collect { it.toSearchRequest() as DsmlMessage },
            processing:     processing,
            responseOrder:  responseOrder,
            onError:        onError,
        )
    }

}
