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
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.hpd.controls.handlers.ConsumerHandler;
import org.openehealth.ipf.commons.ihe.hpd.controls.pagination.ConsumerPaginationHandler;
import org.openehealth.ipf.commons.ihe.hpd.controls.sorting.ConsumerSortingHandler;

/**
 * @author Dmytro Rud
 * @since 4.3
 */
@Slf4j
public class HpdQueryService extends HpdService implements ConsumerHandler<BatchRequest, BatchResponse> {

    private final ConsumerHandler<BatchRequest, BatchResponse> handler;

    public HpdQueryService(HpdQueryEndpoint<WsAuditDataset> endpoint) {
        ConsumerHandler<BatchRequest, BatchResponse> handler = this;
        if (endpoint.isSupportSorting()) {
            handler = new ConsumerSortingHandler(handler);
        }
        if (endpoint.isSupportPagination()) {
            handler = new ConsumerPaginationHandler(handler, endpoint.getPaginationStorage());
        }
        this.handler = handler;
    }

    @Override
    public ConsumerHandler<BatchRequest, BatchResponse> getWrappedHandler() {
        return null;
    }

    @Override
    public BatchResponse handle(BatchRequest request) {
        return super.doProcess(request);
    }

    @Override
    protected BatchResponse doProcess(BatchRequest batchRequest) {
        return handler.handle(batchRequest);
    }

}
