/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti38;

import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.iti38.Iti38PortType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiProducer;

/**
 * Producer for the ITI-38 component.
 */
public class Iti38Producer extends DefaultItiProducer<AdhocQueryRequest, AdhocQueryResponse> {

    public Iti38Producer(Iti38Endpoint endpoint, JaxWsClientFactory clientFactory) {
        super(endpoint, clientFactory, true);
    }

    @Override
    protected AdhocQueryResponse callService(Object client, AdhocQueryRequest body) {
        return ((Iti38PortType) client).documentRegistryRegistryStoredQuery(body);
    }
}
