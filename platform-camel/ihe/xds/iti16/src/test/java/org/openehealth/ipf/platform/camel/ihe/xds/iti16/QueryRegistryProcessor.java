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

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.OrganizationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryObjectListType;

/**
 * Processor for a QueryRegistry request used in Tests.
 * <p>
 * Sets the a field on the response with a text provided in the request. Also 
 * adds the prefix to the text that was configured in the constructor.
 *
 * @author Jens Riemschneider
 */
class QueryRegistryProcessor implements Processor {
    private final String prefix;

    /**
     * Constructs the processor.
     * @param prefix
     *          text that should be prefixed when processing the request.
     */
    public QueryRegistryProcessor(String prefix) {
        this.prefix = prefix;
    }

    public void process(Exchange exchange) throws Exception {
        AdhocQueryRequest request = exchange.getIn().getBody(AdhocQueryRequest.class);

        AdhocQueryResponse response = new AdhocQueryResponse();
        RegistryObjectListType registryObjectListType = new RegistryObjectListType();
        List<Object> objectRefOrAssociationOrAuditableEvent = registryObjectListType.getObjectRefOrAssociationOrAuditableEvent();
        OrganizationType orgType = new OrganizationType();
        orgType.setObjectType(prefix + request.getSQLQuery());
        objectRefOrAssociationOrAuditableEvent.add(orgType);
        response.setSQLQueryResult(registryObjectListType);
        Exchanges.resultMessage(exchange).setBody(response);
    }
}
