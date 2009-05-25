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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti42;

import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

/**
 * Processor for a RegisterDocumentSet request used in Tests.
 * <p>
 * Sets the status field on the response with the text provided
 * as comment in the request. Also adds the prefix to the text that was
 * configured in the constructor.
 *
 * @author Jens Riemschneider
 */
class RegisterDocumentSetProcessor implements Processor {
    private final String prefix;

    /**
     * Constructs the processor.
     * @param prefix
     *          text that should be prefixed when processing the request.
     */
    public RegisterDocumentSetProcessor(String prefix) {
        this.prefix = prefix;
    }

    public void process(Exchange exchange) throws Exception {
        SubmitObjectsRequest request = exchange.getIn().getBody(SubmitObjectsRequest.class);
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(prefix + request.getComment());
        Exchanges.resultMessage(exchange).setBody(response);
    }
}
