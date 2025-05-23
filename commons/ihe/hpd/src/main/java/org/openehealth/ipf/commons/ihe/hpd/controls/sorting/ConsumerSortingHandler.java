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
package org.openehealth.ipf.commons.ihe.hpd.controls.sorting;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.ihe.hpd.HpdUtils;
import org.openehealth.ipf.commons.ihe.hpd.controls.ConsumerHpdHandler;
import org.openehealth.ipf.commons.ihe.hpd.controls.ControlUtils;
import org.openehealth.ipf.commons.ihe.hpd.controls.handlers.ConsumerHandler;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*;

import javax.naming.ldap.SortControl;
import jakarta.xml.bind.JAXBElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Server-side handler of sorting controls.
 *
 * @author Dmytro Rud
 * @since 4.3
 */
@Slf4j
public class ConsumerSortingHandler extends ConsumerHpdHandler {

    public ConsumerSortingHandler(ConsumerHandler<BatchRequest, BatchResponse> wrappedHandler) {
        super(wrappedHandler);
    }

    @Override
    public BatchResponse handle(BatchRequest batchRequest) {
        Map<String, SortControl2> controls = new HashMap<>();

        // responses computed locally, i.e. not obtained from the route
        JAXBElement<?>[] localResponses = new JAXBElement[batchRequest.getBatchRequests().size()];

        for (var position = batchRequest.getBatchRequests().size() - 1; position >= 0; --position) {
            var request = batchRequest.getBatchRequests().get(position);
            var requestId = StringUtils.trimToNull(request.getRequestID());

            if (requestId == null) {
                log.warn("Request ID is missing in {} --> cannot handle, pass to the route as is", request.getClass().getSimpleName());

            } else if (request instanceof SearchRequest) {
                try {
                    SortControl2 sorting = ControlUtils.extractControl(request, SortControl.OID);
                    if (sorting == null) {
                        log.debug("No sorting control in request with ID {} --> pass it to the route", requestId);
                    } else if (sorting.getKeys().length == 0) {
                        log.debug("No sorting keys specified in request with ID {} --> pass it to the route", requestId);
                    } else {
                        log.debug("Sorting control in request with ID {} --> pass it to the route and handle response", requestId);
                        controls.put(requestId, sorting);
                    }

                } catch (Exception e) {
                    log.error("Exception while handling request with ID {} --> create error response", requestId, e);
                    localResponses[position] = HpdUtils.errorResponse(e, requestId);
                    batchRequest.getBatchRequests().remove(position);
                }

            } else {
                log.debug("Pass {} with request ID {} to the route as is", request.getClass().getSimpleName(), requestId);
            }
        }

        var batchResponse = getWrappedHandler().handle(batchRequest);

        for (var position = batchResponse.getBatchResponses().size() - 1; position >= 0; --position) {
            var jaxbElement = batchResponse.getBatchResponses().get(position);
            var value = jaxbElement.getValue();
            var requestId = HpdUtils.extractResponseRequestId(value);

            if (value instanceof SearchResponse searchResponse) {
                var sorting = (requestId == null) ? null : controls.get(requestId);

                if (sorting == null) {
                    log.debug("No sorting was requested for request with ID {} --> return response as is", requestId);

                } else {
                    try {
                        log.debug("Sort items in response with ID {}", requestId);
                        SearchResponseSorter.sort(searchResponse, sorting);
                    } catch (IOException e) {
                        log.error("Exception while handling response with ID {}", requestId, e);
                        batchResponse.getBatchResponses().set(position, HpdUtils.errorResponse(e, requestId));
                    }
                }
            } else {
                log.debug("Return {} with request ID {} as is", value.getClass().getSimpleName(), requestId);
            }
        }

        return aggregateResponse(batchRequest, batchResponse, localResponses);
    }

}
