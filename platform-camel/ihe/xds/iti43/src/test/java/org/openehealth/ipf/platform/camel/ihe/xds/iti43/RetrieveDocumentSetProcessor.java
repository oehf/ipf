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
package org.openehealth.ipf.platform.camel.ihe.xds.iti43;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.LargeDataSource;

import javax.activation.DataHandler;

/**
 * Processor for a RetrieveDocumentSet request used in Tests.
 *
 * @author Jens Riemschneider
 */
class RetrieveDocumentSetProcessor implements Processor {
    private final String expectedValue;

    /**
     * Constructs the processor.
     * @param expectedValue
     *          text that is expected as the first document unique ID.
     */
    public RetrieveDocumentSetProcessor(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void process(Exchange exchange) throws Exception {
        RetrieveDocumentSet request = exchange.getIn().getBody(RetrieveDocumentSet.class);
        RetrieveDocument retrieveDocument = request.getDocuments().get(0);
        String value = retrieveDocument.getDocumentUniqueId();
        RetrievedDocumentSet response = new RetrievedDocumentSet();
        response.setStatus(Status.SUCCESS);
        if (!expectedValue.equals(value)) {
            response.setStatus(Status.FAILURE);
        }
        else {
            RetrievedDocument doc = new RetrievedDocument();
            doc.setDataHandler(new DataHandler(new LargeDataSource()));
            doc.setRequestData(retrieveDocument);
            response.getDocuments().add(doc);
        }
        
        Exchanges.resultMessage(exchange).setBody(response);
    }
}
