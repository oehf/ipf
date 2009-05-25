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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti41;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.utils.CxfTestUtils;
import org.openehealth.ipf.platform.camel.ihe.xdsb.iti41.service.ProvideAndRegisterDocumentSetRequestType;

import javax.activation.DataHandler;
import java.util.List;

/**
 * Processor for a ProvideAndRegisterDocumentSet request used in Tests.
 * <p>
 * Sets the status field on the response with the text provided as comment in
 * the request. Also adds the prefix to the text that was configured in the
 * constructor.
 * <p>
 * If the comment in the request is {@code large} this processor will read the
 * whole content of the data source in each document. This is used to find out
 * if the underlying infrastructure supports memory efficient streaming.
 * <p>
 * The status field is set to {@code ok} prefixed with the given prefix value
 * if the content could be read completely. It is set to {@code Only read: NN}
 * if the content was not fully read.
 *
 * @author Jens Riemschneider
 */
class ProvideAndRegisterDocumentSetProcessor implements Processor {
    private final String prefix;

    /**
     * Constructs the processor.
     * @param prefix
     *          text that should be prefixed when processing the request.
     */
    public ProvideAndRegisterDocumentSetProcessor(String prefix) {
        this.prefix = prefix;
    }

    public void process(Exchange exchange) throws Exception {
        ProvideAndRegisterDocumentSetRequestType request = exchange.getIn().getBody(ProvideAndRegisterDocumentSetRequestType.class);
        String value = request.getSubmitObjectsRequest().getComment();

        if (value.equals("large")) {
            // Note: Only check for MTOM if this was indeed a large content
            // CXF might use SwA if the content is too small.
            List<ProvideAndRegisterDocumentSetRequestType.Document> documents = request.getDocument();
            for (ProvideAndRegisterDocumentSetRequestType.Document document : documents) {
                DataHandler dataHandler = document.getValue();
                if (!CxfTestUtils.isCxfUsingMtom(dataHandler)) {
                    value = "Was not using MTOM";
                }
                else {
                    value = "ok";
                }
            }
        }

        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(prefix + value);
        Exchanges.resultMessage(exchange).setBody(response);
    }
}
