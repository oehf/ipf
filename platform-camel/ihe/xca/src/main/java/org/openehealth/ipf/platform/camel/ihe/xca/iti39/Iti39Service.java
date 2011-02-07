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
package org.openehealth.ipf.platform.camel.ihe.xca.iti39;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xca.iti39.Iti39PortType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.core.DefaultXdsWebService;
import org.openehealth.ipf.platform.camel.ihe.xds.core.converters.EbXML30Converters;

import javax.jws.WebParam;

/**
 * Service implementation for the IHE ITI-39 transaction.
 */
public class Iti39Service extends DefaultXdsWebService implements Iti39PortType {

    @Override
    public RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(@WebParam(partName = "body", name = "RetrieveDocumentSetRequest", targetNamespace = "urn:ihe:iti:xds-b:2007") RetrieveDocumentSetRequestType body) {
        Exchange result = process(body);
        if (result.getException() != null) {
            RetrievedDocumentSet errorResponse = new RetrievedDocumentSet();
            configureError(errorResponse, result.getException(), ErrorCode.REPOSITORY_METADATA_ERROR, ErrorCode.REPOSITORY_ERROR);
            return EbXML30Converters.convert(errorResponse);
        }

        return Exchanges.resultMessage(result).getBody(RetrieveDocumentSetResponseType.class);
    }
}
