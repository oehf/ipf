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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.iti43.Iti43PortType;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsRetrieveDocumentSetService;

/**
 * Service implementation for the IHE ITI-43 transaction (Retrieve Document Set).
 * <p/>
 * This implementation delegates to a Camel consumer by creating an exchange.
 *
 * @author Jens Riemschneider
 */
public class Iti43Service extends XdsRetrieveDocumentSetService<RetrieveDocumentSetRequestType> implements Iti43PortType {

    public Iti43Service() {
        super(null);
    }

    @Override
    public RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(RetrieveDocumentSetRequestType body) {
        return processRequest(body);
    }
}
