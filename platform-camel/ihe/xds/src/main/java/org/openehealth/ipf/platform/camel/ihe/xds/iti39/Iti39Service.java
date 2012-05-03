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

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xds.iti39.Iti39PortType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.xds.core.converters.EbXML30Converters;

/**
 * Service implementation for the IHE ITI-39 transaction.
 */
@Slf4j
public class Iti39Service extends AbstractWebService implements Iti39PortType {
    private final AbstractWsEndpoint endpoint;

    public Iti39Service(AbstractWsEndpoint endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(RetrieveDocumentSetRequestType body) {
        Exchange result = process(body);
        if (result.getException() != null) {
            log.debug("ITI-39 service failed", result.getException());
            RetrievedDocumentSet errorResponse = new RetrievedDocumentSet(
                    result.getException(),
                    ErrorCode.REPOSITORY_METADATA_ERROR,
                    ErrorCode.REPOSITORY_ERROR,
                    endpoint.getHomeCommunityId());
            errorResponse.getErrors().get(0).setLocation(endpoint.getHomeCommunityId());
            return EbXML30Converters.convert(errorResponse);
        }

        return Exchanges.resultMessage(result).getBody(RetrieveDocumentSetResponseType.class);
    }
}
