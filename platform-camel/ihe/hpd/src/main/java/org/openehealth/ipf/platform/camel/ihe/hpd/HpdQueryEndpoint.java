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

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.hpd.controls.pagination.PaginationStorage;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import java.util.Map;

/**
 * @author Dmytro Rud
 * @since 3.7.5
 */
abstract public class HpdQueryEndpoint<AuditDatasetType extends WsAuditDataset> extends HpdEndpoint<AuditDatasetType> {

    @Getter
    @Setter
    private boolean supportSorting = false;

    @Getter
    @Setter
    private boolean supportPagination = false;

    @Getter
    @Setter
    private PaginationStorage paginationStorage = null;

    @Getter
    @Setter
    private int defaultPageSize = 100;

    public HpdQueryEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<AuditDatasetType, WsTransactionConfiguration<AuditDatasetType>, ? extends WsInteractionId<WsTransactionConfiguration<AuditDatasetType>>> component,
            Map<String, Object> parameters,
            Class<? extends HpdQueryService> serviceClass)
    {
        super(endpointUri, address, component, parameters, serviceClass);
    }

    @Override
    public AbstractWsProducer<AuditDatasetType, WsTransactionConfiguration<AuditDatasetType>, ?, ?> getProducer(AbstractWsEndpoint<AuditDatasetType, WsTransactionConfiguration<AuditDatasetType>> endpoint, JaxWsClientFactory<AuditDatasetType> clientFactory) {
        return new HpdQueryProducer<>((HpdQueryEndpoint<AuditDatasetType>) endpoint, clientFactory);
    }

}
