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
package org.openehealth.ipf.commons.ihe.hpd.controls.pagination;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.ihe.hpd.HpdException;
import org.openehealth.ipf.commons.ihe.hpd.HpdUtils;
import org.openehealth.ipf.commons.ihe.hpd.controls.ConsumerHpdHandler;
import org.openehealth.ipf.commons.ihe.hpd.controls.ControlUtils;
import org.openehealth.ipf.commons.ihe.hpd.controls.handlers.ConsumerHandler;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*;

import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;
import jakarta.xml.bind.JAXBElement;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Server-side handler of pagination controls.
 *
 * @author Dmytro Rud
 * @since 4.3
 */
@Slf4j
public class ConsumerPaginationHandler extends ConsumerHpdHandler {

    private final PaginationStorage paginationStorage;

    public ConsumerPaginationHandler(ConsumerHandler<BatchRequest, BatchResponse> wrappedHandler, PaginationStorage paginationStorage) {
        super(wrappedHandler);
        this.paginationStorage = Objects.requireNonNull(paginationStorage, "Pagination storage must be provided");
    }

    @Override
    public BatchResponse handle(BatchRequest batchRequest) {
        Map<String, PagedResultsResponseControl> controls = new HashMap<>();

        // responses computed locally, i.e. not obtained from the route
        JAXBElement<?>[] localResponses = new JAXBElement[batchRequest.getBatchRequests().size()];

        for (var position = batchRequest.getBatchRequests().size() - 1; position >= 0; --position) {
            var request = batchRequest.getBatchRequests().get(position);
            var requestId = StringUtils.trimToNull(request.getRequestID());

            if (requestId == null) {
                log.warn("Request ID is missing in {} --> cannot handle, pass to the route as is", request.getClass().getSimpleName());

            } else if (request instanceof SearchRequest) {
                try {
                    PagedResultsResponseControl pagination = ControlUtils.extractControl(request, PagedResultsResponseControl.OID);
                    if (pagination == null) {
                        log.debug("No pagination control in request with ID {} --> pass it to the route", requestId);
                    } else {
                        if (pagination.getResultSize() < 1) {
                            log.debug("Non-positive page length in request with ID {} --> create error response", requestId);
                            localResponses[position] = HpdUtils.errorResponse(new HpdException("Page size must be positive", ErrorResponse.ErrorType.MALFORMED_REQUEST), requestId);
                            batchRequest.getBatchRequests().remove(position);
                        } else if (pagination.getCookie() == null) {
                            log.debug("Initial pagination control in request with ID {} --> pass it to the route and handle response", requestId);
                            controls.put(requestId, pagination);
                        } else {
                            var take = paginationStorage.take(pagination);
                            if (take.getEntries().isEmpty()) {
                                log.debug("Cannot serve request with ID {} from the storage --> pass it to the route", requestId);
                            } else {
                                log.debug("Serve request with ID {} from the storage", requestId);
                                var response = new SearchResponse();
                                response.setRequestID(requestId);
                                response.getSearchResultEntry().addAll(take.getEntries());
                                response.setSearchResultDone(new LDAPResult());
                                response.getSearchResultDone().setResultCode(new ResultCode());
                                response.getSearchResultDone().getResultCode().setCode(0);
                                ControlUtils.setControl(response, new PagedResultsControl(0, take.isMoreEntriesAvailable() ? pagination.getCookie() : null, true));

                                localResponses[position] = HpdUtils.DSMLV2_OBJECT_FACTORY.createBatchResponseSearchResponse(response);
                                batchRequest.getBatchRequests().remove(position);
                            }
                        }
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

        if (batchRequest.getBatchRequests().isEmpty()) {
            log.debug("No requests left for processing --> send nothing to the route");
            return aggregateResponse(batchRequest, new BatchResponse(), localResponses);
        }

        var batchResponse = getWrappedHandler().handle(batchRequest);

        for (var position = batchResponse.getBatchResponses().size() - 1; position >= 0; --position) {
            var jaxbElement = batchResponse.getBatchResponses().get(position);
            var value = jaxbElement.getValue();
            var requestId = StringUtils.trimToNull(HpdUtils.extractResponseRequestId(value));

            if (value instanceof SearchResponse searchResponse) {
                var pagination = (requestId == null) ? null : controls.get(requestId);

                if (pagination == null) {
                    log.debug("No pagination was requested for request with ID {} --> return response as is", requestId);

                } else {
                    try {
                        var entries = searchResponse.getSearchResultEntry();
                        var entriesCount = entries.size();
                        var requestedCount = pagination.getResultSize();

                        if (entriesCount <= requestedCount) {
                            log.debug("Pagination of request with ID {} finished", requestId);
                            ControlUtils.setControl(searchResponse, new PagedResultsControl(0, null, true));
                        } else {
                            log.debug("For request with ID {}, return first {} entries and store {} more in the storage",
                                    requestId, requestedCount, entriesCount - requestedCount);

                            List<SearchResultEntry> entriesToStore = new ArrayList<>(entries.subList(requestedCount, entriesCount));
                            var cookie = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
                            paginationStorage.store(cookie, entriesToStore);

                            List<SearchResultEntry> entriesToDeliver = new ArrayList<>(entries.subList(0, requestedCount));
                            searchResponse.getSearchResultEntry().clear();
                            searchResponse.getSearchResultEntry().addAll(entriesToDeliver);
                            ControlUtils.setControl(searchResponse, new PagedResultsControl(0, cookie, true));
                        }

                    } catch (Exception e) {
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
