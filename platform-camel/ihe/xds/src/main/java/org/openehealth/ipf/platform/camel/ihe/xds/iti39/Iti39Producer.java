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
package org.openehealth.ipf.platform.camel.ihe.xds.iti39;

import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.iti39.Iti39PortType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiProducer;

/**
 * Producer for the ITI-39 component.
 */
public class Iti39Producer extends DefaultItiProducer<RetrieveDocumentSetRequestType, RetrieveDocumentSetResponseType> {

    public Iti39Producer(Iti39Endpoint endpoint, JaxWsClientFactory clientFactory) {
        super(endpoint, clientFactory, true);
    }

    @Override
    protected RetrieveDocumentSetResponseType callService(Object client, RetrieveDocumentSetRequestType body) {
        return ((Iti39PortType) client).documentRepositoryRetrieveDocumentSet(body);
    }
}
