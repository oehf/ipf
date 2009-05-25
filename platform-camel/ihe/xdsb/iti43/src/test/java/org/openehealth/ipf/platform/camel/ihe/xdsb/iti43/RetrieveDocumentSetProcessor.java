/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti43;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.utils.LargeDataSource;
import org.openehealth.ipf.platform.camel.ihe.xdsb.iti43.service.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.platform.camel.ihe.xdsb.iti43.service.RetrieveDocumentSetResponseType;

import javax.activation.DataHandler;
import javax.activation.DataSource;

/**
 * Processor for a RetrieveDocumentSet request used in Tests.
 * <p>
 * Sets the status field on the response with the text provided as document
 * unique ID of the first document in the request. Also adds the prefix to
 * the text that was configured in the constructor.
 * <p>
 * If the document unique ID in the request is {@code large} this processor
 * will provide a large stream in the first document of the response. This
 * is used to find out if the underlying infrastructure supports memory
 * efficient streaming.
 *
 * @author Jens Riemschneider
 */
class RetrieveDocumentSetProcessor implements Processor {
    private final String prefix;

    /**
     * Constructs the processor.
     * @param prefix
     *          text that should be prefixed when processing the request.
     */
    public RetrieveDocumentSetProcessor(String prefix) {
        this.prefix = prefix;
    }

    public void process(Exchange exchange) throws Exception {
        RetrieveDocumentSetRequestType request = exchange.getIn().getBody(RetrieveDocumentSetRequestType.class);
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType registryResponse = new RegistryResponseType();
        String value = request.getDocumentRequest().get(0).getDocumentUniqueId();
        registryResponse.setStatus(prefix + value);
        response.setRegistryResponse(registryResponse);
        if (value.equals("large")) {
            RetrieveDocumentSetResponseType.DocumentResponse documentResponse = new RetrieveDocumentSetResponseType.DocumentResponse();
            DataSource dataSource = new LargeDataSource();
            documentResponse.setDocument(new DataHandler(dataSource));
            response.getDocumentResponse().add(documentResponse);
        }
        Exchanges.resultMessage(exchange).setBody(response);
    }
}
