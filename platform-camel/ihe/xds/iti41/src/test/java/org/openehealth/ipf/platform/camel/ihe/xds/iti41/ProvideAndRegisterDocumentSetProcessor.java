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
package org.openehealth.ipf.platform.camel.ihe.xds.iti41;

import java.io.InputStream;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Document;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.CxfTestUtils;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.LargeDataSource;

/**
 * Processor for a ProvideAndRegisterDocumentSet request used in Tests.
 * <p>
 * Sets the status field on the response with a text provided in the request. 
 * Also adds the prefix to the text that was configured in the constructor.
 *
 * @author Jens Riemschneider
 */
class ProvideAndRegisterDocumentSetProcessor implements Processor {
    private final String expectedValue;

    /**
     * Constructs the processor.
     * @param expectedValue
     *          text that is expected to be send within the comment of the first
     *          document entry.
     */
    public ProvideAndRegisterDocumentSetProcessor(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void process(Exchange exchange) throws Exception {
        Document doc = exchange.getIn().getBody(ProvideAndRegisterDocumentSet.class).getDocuments().get(0);
        String value = doc.getDocumentEntry().getComments().getValue();        
        Response response = new Response();
        Status status = Status.SUCCESS;
        DataHandler dataHandler = doc.getDataHandler();
        if (!expectedValue.equals(value) || dataHandler == null) {
            status = Status.FAILURE;
        }
        else {
            InputStream inputStream = doc.getDataHandler().getInputStream();
            try {
                if (!CxfTestUtils.isCxfUsingMtom(inputStream)) {
                    status = Status.FAILURE;
                }
                else {
                    int length = 0;
                    while (inputStream.read() != -1) {
                        ++length;
                    }
                    if (length != LargeDataSource.STREAM_SIZE) {
                        status = Status.FAILURE;
                    }
                }
            }
            finally {
                inputStream.close();
            }
        }
        
        response.setStatus(status);
        Exchanges.resultMessage(exchange).setBody(response);
    }
}
