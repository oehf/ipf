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
package org.openehealth.ipf.platform.camel.ihe.xds.iti17;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

/**
 * Processor for a RetrieveDocument request used in Tests.
 * <p>
 * Sets the file content stream within the exchange based on the
 * download URL. Adds the prefix specified in the constructor to the
 * beginning of the content stream.
 *
 * @author Jens Riemschneider
 */
class RetrieveDocumentProcessor implements Processor {
    private final String prefix;

    /**
     * Constructs the processor.
     * @param prefix
     *          text that should be prefixed when processing the request.
     */
    public RetrieveDocumentProcessor(String prefix) {
        this.prefix = prefix;
    }

    public void process(Exchange exchange) throws Exception {
        String requestUri = exchange.getIn().getBody(String.class);
        int queryStart = requestUri.indexOf('?') + 1;        
        String content = prefix + requestUri.substring(queryStart);        
        Exchanges.resultMessage(exchange).setBody(IOUtils.toInputStream(content));
    }
}
