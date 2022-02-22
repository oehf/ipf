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
import org.openehealth.ipf.commons.ihe.hpd.HpdException;
import org.openehealth.ipf.commons.ihe.hpd.controls.ControlUtils;
import org.openehealth.ipf.commons.ihe.hpd.controls.pagination.Pagination;
import org.openehealth.ipf.commons.ihe.hpd.controls.pagination.PaginationStorage;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*;

import javax.xml.bind.JAXBElement;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.openehealth.ipf.commons.ihe.hpd.controls.pagination.PaginationStorage.TakeResult;

@Slf4j
abstract public class HpdQueryService extends HpdService {

    private final boolean supportPagination;
    private final PaginationStorage paginationStorage;

    protected HpdQueryService(HpdQueryEndpoint endpoint) {
        this.supportPagination = endpoint.isSupportPagination();
        this.paginationStorage = endpoint.getPaginationStorage();

        if (supportPagination && (paginationStorage == null)) {
            throw new IllegalArgumentException("Pagination storage must be provided if supportPagination is set to true");
        }
    }

    @Override
    protected BatchResponse doProcess(BatchRequest batchRequest) {
        if (!supportPagination) {
            log.debug("Pagination is not supported");
            return super.doProcess(batchRequest);
        }

        BatchResponse result = new BatchResponse();
        result.setRequestID(batchRequest.getRequestID());

        Map<String, Pagination> paginationRequests = new HashMap<>();

        Iterator<DsmlMessage> iterator = batchRequest.getBatchRequests().iterator();
        while (iterator.hasNext()) {
            DsmlMessage request = iterator.next();
            String requestId = request.getRequestID();

            if (request instanceof SearchRequest) {
                try {
                    Pagination pagination = ControlUtils.extractControl(request, Pagination.TYPE);
                    if (pagination == null) {
                        log.debug("No pagination control in request with ID {} --> send it to the route", requestId);
                    } else {
                        if (pagination.getSize() < 1) {
                            throw new HpdException("Page size must be positive", ErrorResponse.ErrorType.MALFORMED_REQUEST);
                        } else if (pagination.isEmptyCookie()) {
                            log.debug("Initial pagination control in request with ID {} --> send it to the route and handle response", requestId);
                            paginationRequests.put(requestId, pagination);
                        } else {
                            TakeResult take = paginationStorage.take(pagination);
                            if (take.getEntries().isEmpty()) {
                                log.debug("Cannot serve request with ID {} from the storage --> send it to the route", requestId);
                            } else {
                                log.debug("Serve request with ID {} from the storage", requestId);
                                SearchResponse response = new SearchResponse();
                                response.setRequestID(requestId);
                                response.getSearchResultEntry().addAll(take.getEntries());
                                response.setSearchResultDone(new LDAPResult());
                                response.getSearchResultDone().setResultCode(new ResultCode());
                                response.getSearchResultDone().getResultCode().setCode(0);
                                ControlUtils.setControl(response, new Pagination(0, take.isMoreEntriesAvailable() ? pagination.getCookie() : null, true));

                                result.getBatchResponses().add(DSMLV2_OBJECT_FACTORY.createBatchResponseSearchResponse(response));
                                iterator.remove();
                            }
                        }
                    }

                } catch (Exception e) {
                    log.error("Exception while handling request with ID {}", requestId, e);
                    ErrorResponse errorResponse = errorResponse(e, requestId);
                    result.getBatchResponses().add(DSMLV2_OBJECT_FACTORY.createBatchResponseErrorResponse(errorResponse));
                    iterator.remove();
                }

            } else {
                log.debug("Pass {} with request ID {} as is", request.getClass().getSimpleName(), requestId);
            }
        }

        if (batchRequest.getBatchRequests().isEmpty()) {
            log.debug("No requests left for processing --> send nothing to the route");
            return result;
        }

        BatchResponse batchResponse = super.doProcess(batchRequest);

        for (JAXBElement<?> jaxbElement : batchResponse.getBatchResponses()) {
            Object value = jaxbElement.getValue();
            String requestId = ControlUtils.extractResponseRequestId(value);

            if (value instanceof SearchResponse) {
                SearchResponse searchResponse = (SearchResponse) value;
                Pagination pagination = paginationRequests.get(requestId);

                if (pagination == null) {
                    log.debug("No pagination was requested for request with ID {} --> pass response as is", requestId);
                    result.getBatchResponses().add(jaxbElement);
                } else {
                    try {
                        List<SearchResultEntry> entries = searchResponse.getSearchResultEntry();
                        int entriesCount = entries.size();

                        if (entriesCount <= pagination.getSize()) {
                            log.debug("Pagination of request with ID {} finished", requestId);
                            ControlUtils.setControl(searchResponse, new Pagination(0, null, true));
                        } else {
                            log.debug("For request with ID {}, return first {} entries and store {} more in the storage",
                                    requestId, pagination.getSize(), entriesCount - pagination.getSize());

                            List<SearchResultEntry> entriesToStore = new ArrayList<>(entries.subList(pagination.getSize(), entriesCount));
                            byte[] cookie = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
                            paginationStorage.store(cookie, entriesToStore);

                            List<SearchResultEntry> entriesToDeliver = new ArrayList<>(entries.subList(0, pagination.getSize()));
                            searchResponse.getSearchResultEntry().clear();
                            searchResponse.getSearchResultEntry().addAll(entriesToDeliver);
                            ControlUtils.setControl(searchResponse, new Pagination(0, cookie, true));
                        }

                        result.getBatchResponses().add(jaxbElement);

                    } catch (Exception e) {
                        log.error("Exception while handling response with ID {}", requestId, e);
                        ErrorResponse errorResponse = errorResponse(e, requestId);
                        result.getBatchResponses().add(DSMLV2_OBJECT_FACTORY.createBatchResponseErrorResponse(errorResponse));
                    }
                }
            } else {
                log.debug("Pass {} with request ID {} as is", value.getClass().getSimpleName(), requestId);
                result.getBatchResponses().add(jaxbElement);
            }
        }

        return result;
    }

}
