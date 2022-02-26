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
package org.openehealth.ipf.platform.camel.ihe.hpd;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.ihe.hpd.HpdException;
import org.openehealth.ipf.commons.ihe.hpd.HpdUtils;
import org.openehealth.ipf.commons.ihe.hpd.controls.ControlUtils;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*;

import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;
import javax.xml.bind.JAXBElement;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.openehealth.ipf.commons.ihe.hpd.controls.pagination.PaginationStorage.TakeResult;

/**
 * @author Dmytro Rud
 * @since 3.7.5
 */
@Slf4j
public class HpdQueryService extends HpdQueryService0 {

    protected HpdQueryService(HpdQueryEndpoint<?> endpoint) {
        super(endpoint);

        if (endpoint.isSupportPagination() && (endpoint.getPaginationStorage() == null)) {
            throw new IllegalArgumentException("Pagination storage must be provided if supportPagination is set to true");
        }
    }

    @Override
    protected BatchResponse doProcess(BatchRequest batchRequest) {
        if (!endpoint.isSupportPagination()) {
            log.debug("Pagination is not supported");
            return super.doProcess(batchRequest);
        }
        return new Handler(this, batchRequest).handle();
    }

    private BatchResponse superDoProcess(BatchRequest batchRequest) {
        return super.doProcess(batchRequest);
    }


    private static class Handler {
        private final HpdQueryService service;
        private final BatchRequest batchRequest;

        // map from request ID to pagination control
        private final Map<String, PagedResultsResponseControl> paginations;

        // map from request ID to position in the list
        private final Map<String, Integer> requestPositions;

        // responses for requests with known positions
        private final JAXBElement<?>[] positionedResponses;

        // responses without request IDs or with request IDs not present in batch request
        private final List<JAXBElement<?>> unpositionedResponses;

        private Handler(HpdQueryService service, BatchRequest batchRequest) {
            this.service = service;
            this.batchRequest = batchRequest;
            this.paginations = new HashMap<>();
            this.requestPositions = new HashMap<>();
            this.positionedResponses = new JAXBElement[batchRequest.getBatchRequests().size()];
            this.unpositionedResponses = new ArrayList<>();
        }

        private BatchResponse createBatchResponse() {
            BatchResponse batchResponse = new BatchResponse();
            batchResponse.setRequestID(batchRequest.getRequestID());

            Iterator<JAXBElement<?>> unpositionedIterator = unpositionedResponses.iterator();

            for (JAXBElement<?> response : positionedResponses) {
                if (response != null) {
                    batchResponse.getBatchResponses().add(response);
                } else if (unpositionedIterator.hasNext()) {
                    batchResponse.getBatchResponses().add(unpositionedIterator.next());
                }
            }

            while (unpositionedIterator.hasNext()) {
                batchResponse.getBatchResponses().add(unpositionedIterator.next());
            }

            return batchResponse;
        }

        /**
         * Set a response locally, instead of querying the actual backend
         */
        private void presetResponse(int position, JAXBElement<?> response) {
            positionedResponses[position] = response;
            batchRequest.getBatchRequests().remove(position);
        }

        private void setResponse(String requestId, JAXBElement<?> response) {
            if ((requestId != null) && requestPositions.containsKey(requestId)) {
                positionedResponses[requestPositions.get(requestId)] = response;
            } else {
                unpositionedResponses.add(response);
            }
        }

        private BatchResponse handle() {
            for (int position = batchRequest.getBatchRequests().size() - 1; position >= 0; --position) {
                DsmlMessage request = batchRequest.getBatchRequests().get(position);
                String requestId = StringUtils.trimToNull(request.getRequestID());

                if (requestId == null) {
                    log.warn("Request ID is missing in {} --> cannot handle, pass to the route as is", request.getClass().getSimpleName());

                } else if (request instanceof SearchRequest) {
                    requestPositions.put(requestId, position);

                    try {
                        PagedResultsResponseControl pagination = ControlUtils.extractControl(request, PagedResultsResponseControl.OID);
                        if (pagination == null) {
                            log.debug("No pagination control in request with ID {} --> pass it to the route", requestId);
                        } else {
                            if (pagination.getResultSize() < 1) {
                                log.debug("Non-positive page length in request with ID {} --> create error response", requestId);
                                presetResponse(position, HpdUtils.errorResponse(new HpdException("Page size must be positive", ErrorResponse.ErrorType.MALFORMED_REQUEST), requestId));
                            } else if (pagination.getCookie() == null) {
                                log.debug("Initial pagination control in request with ID {} --> pass it to the route and handle response", requestId);
                                paginations.put(requestId, pagination);
                            } else {
                                TakeResult take = service.endpoint.getPaginationStorage().take(pagination);
                                if (take.getEntries().isEmpty()) {
                                    log.debug("Cannot serve request with ID {} from the storage --> pass it to the route", requestId);
                                } else {
                                    log.debug("Serve request with ID {} from the storage", requestId);
                                    SearchResponse response = new SearchResponse();
                                    response.setRequestID(requestId);
                                    response.getSearchResultEntry().addAll(take.getEntries());
                                    response.setSearchResultDone(new LDAPResult());
                                    response.getSearchResultDone().setResultCode(new ResultCode());
                                    response.getSearchResultDone().getResultCode().setCode(0);
                                    ControlUtils.setControl(response, new PagedResultsControl(0, take.isMoreEntriesAvailable() ? pagination.getCookie() : null, true));

                                    presetResponse(position, HpdUtils.DSMLV2_OBJECT_FACTORY.createBatchResponseSearchResponse(response));
                                }
                            }
                        }

                    } catch (Exception e) {
                        log.error("Exception while handling request with ID {} --> create error response", requestId, e);
                        presetResponse(position, HpdUtils.errorResponse(e, requestId));
                    }

                } else {
                    log.debug("Pass {} with request ID {} to the route as is", request.getClass().getSimpleName(), requestId);
                }
            }

            if (batchRequest.getBatchRequests().isEmpty()) {
                log.debug("No requests left for processing --> send nothing to the route");
                return createBatchResponse();
            }

            BatchResponse batchResponse = service.superDoProcess(batchRequest);

            for (JAXBElement<?> jaxbElement : batchResponse.getBatchResponses()) {
                Object value = jaxbElement.getValue();
                String requestId = StringUtils.trimToNull(HpdUtils.extractResponseRequestId(value));

                if (value instanceof SearchResponse) {
                    SearchResponse searchResponse = (SearchResponse) value;
                    PagedResultsResponseControl pagination = (requestId == null) ? null : paginations.get(requestId);

                    if (pagination == null) {
                        log.debug("No pagination was requested for request with ID {} --> return response as is", requestId);
                        setResponse(requestId, jaxbElement);
                    } else {
                        try {
                            List<SearchResultEntry> entries = searchResponse.getSearchResultEntry();
                            int entriesCount = entries.size();
                            int requestedCount = pagination.getResultSize();

                            if (entriesCount <= requestedCount) {
                                log.debug("Pagination of request with ID {} finished", requestId);
                                ControlUtils.setControl(searchResponse, new PagedResultsControl(0, null, true));
                            } else {
                                log.debug("For request with ID {}, return first {} entries and store {} more in the storage",
                                        requestId, requestedCount, entriesCount - requestedCount);

                                List<SearchResultEntry> entriesToStore = new ArrayList<>(entries.subList(requestedCount, entriesCount));
                                byte[] cookie = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
                                service.endpoint.getPaginationStorage().store(cookie, entriesToStore);

                                List<SearchResultEntry> entriesToDeliver = new ArrayList<>(entries.subList(0, requestedCount));
                                searchResponse.getSearchResultEntry().clear();
                                searchResponse.getSearchResultEntry().addAll(entriesToDeliver);
                                ControlUtils.setControl(searchResponse, new PagedResultsControl(0, cookie, true));
                            }

                            setResponse(requestId, jaxbElement);

                        } catch (Exception e) {
                            log.error("Exception while handling response with ID {}", requestId, e);
                            setResponse(requestId, HpdUtils.errorResponse(e, requestId));
                        }
                    }
                } else {
                    log.debug("Return {} with request ID {} as is", value.getClass().getSimpleName(), requestId);
                    setResponse(requestId, jaxbElement);
                }
            }

            return createBatchResponse();
        }
    }

}
