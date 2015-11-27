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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.iti39.Iti39PortType;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsRetrieveDocumentSetService;

/**
 * Service implementation for the IHE ITI-39 transaction.
 */
public class Iti39Service extends XdsRetrieveDocumentSetService<RetrieveDocumentSetRequestType> implements Iti39PortType {

    public Iti39Service(String homeCommunityId) {
        super(homeCommunityId);
    }

    @Override
    public RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(RetrieveDocumentSetRequestType body) {
        return processRequest(body);
    }
}
