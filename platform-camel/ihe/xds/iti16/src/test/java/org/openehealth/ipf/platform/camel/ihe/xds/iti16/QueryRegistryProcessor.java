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
package org.openehealth.ipf.platform.camel.ihe.xds.iti16;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.QueryRegistry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.SqlQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.QueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;

/**
 * Processor for a QueryRegistry request used in Tests.
 *
 * @author Jens Riemschneider
 */
class QueryRegistryProcessor implements Processor {
    private final String expectedValue;

    /**
     * Constructs the processor.
     * @param expectedValue
     *          text that is expected to be send within the comment of the first
     *          document entry.
     */
    public QueryRegistryProcessor(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void process(Exchange exchange) throws Exception {
        SqlQuery query = (SqlQuery) exchange.getIn().getBody(QueryRegistry.class).getQuery();
        String value = query.getSql();        
        QueryResponse response = new QueryResponse();
        response.setStatus(expectedValue.equals(value) ? Status.SUCCESS : Status.FAILURE);
        Exchanges.resultMessage(exchange).setBody(response);
    }
}
