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
package org.openehealth.ipf.platform.camel.ihe.xds.iti14;

import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;

/**
 * Processor for a RegisterDocumentSet request used in Tests.
 *
 * @author Jens Riemschneider
 */
class RegisterDocumentSetProcessor implements Processor {
     private final String expectedValue;

    /**
     * Constructs the processor.
     * @param expectedValue
     *          text that is expected to be send within the comment of the first
     *          document entry.
     */
    public RegisterDocumentSetProcessor(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void process(Exchange exchange) throws Exception {
        String value = exchange.getIn().getBody(RegisterDocumentSet.class).getDocumentEntries().get(0).getComments().getValue();        
        Response response = new Response();
        response.setStatus(expectedValue.equals(value) ? Status.SUCCESS : Status.FAILURE);
        Exchanges.resultMessage(exchange).setBody(response);
    }
}
