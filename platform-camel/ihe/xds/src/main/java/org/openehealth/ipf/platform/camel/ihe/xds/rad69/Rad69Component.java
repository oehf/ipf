/*
 * Copyright 2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.rad69;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.xdsi.RetrieveImagingDocumentSetRequestType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsComponent;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsEndpoint;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.xds.RAD.Interactions.RAD_69;

/**
 * The Camel component for the RAD-69 transaction.
 */
public class Rad69Component extends XdsComponent<XdsNonconstructiveDocumentSetRequestAuditDataset> {


    public Rad69Component() {
        super(RAD_69);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new XdsEndpoint<>(uri, remaining, this, parameters, Rad69Service.class) {
            @Override
            public AbstractWsProducer<XdsNonconstructiveDocumentSetRequestAuditDataset, WsTransactionConfiguration<XdsNonconstructiveDocumentSetRequestAuditDataset>, ?, ?> getProducer(AbstractWsEndpoint<XdsNonconstructiveDocumentSetRequestAuditDataset, WsTransactionConfiguration<XdsNonconstructiveDocumentSetRequestAuditDataset>> endpoint,
                                                                                                                                                                                        JaxWsClientFactory<XdsNonconstructiveDocumentSetRequestAuditDataset> clientFactory) {
                return new SimpleWsProducer<>(
                        endpoint, clientFactory, RetrieveImagingDocumentSetRequestType.class, RetrieveDocumentSetResponseType.class);
            }
        };
    }

}
