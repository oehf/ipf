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
import org.openehealth.ipf.commons.ihe.hpd.controls.handlers.ProducerHandler;
import org.openehealth.ipf.commons.ihe.hpd.controls.pagination.ProducerPaginationHandler;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;

/**
 * @author Dmytro Rud
 * @since 4.3
 */
@Slf4j
public class HpdQueryProducer<AuditDatasetType extends WsAuditDataset> extends SimpleWsProducer<AuditDatasetType, WsTransactionConfiguration<AuditDatasetType>, BatchRequest, BatchResponse>
        implements ProducerHandler<BatchRequest, BatchResponse>
{

    private final ProducerHandler<BatchRequest, BatchResponse> handler;

    public HpdQueryProducer(HpdQueryEndpoint<AuditDatasetType> endpoint, JaxWsClientFactory<AuditDatasetType> clientFactory) {
        super(endpoint, clientFactory, BatchRequest.class, BatchResponse.class);
        ProducerHandler<BatchRequest, BatchResponse> handler = this;
        if (endpoint.isSupportPagination()) {
            handler = new ProducerPaginationHandler(this, endpoint.getDefaultPageSize());
        }
        this.handler = handler;
    }

    @Override
    public ProducerHandler<BatchRequest, BatchResponse> getWrappedHandler() {
        return null;
    }

    @Override
    public BatchResponse handle(Object clientObject, BatchRequest batchRequest) throws Exception {
        return super.callService(clientObject, batchRequest);
    }

    @Override
    protected BatchResponse callService(Object clientObject, BatchRequest batchRequest) throws Exception {
        return handler.handle(clientObject, batchRequest);
    }

}
