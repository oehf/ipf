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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti43.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 * Provides the ITI-43 web-service interface.
 */
@WebService(targetNamespace = "urn:ihe:iti:xds-b:2007", name = "DocumentRepository_PortType")
@XmlSeeAlso({org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.rim.ObjectFactory.class, org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.lcm.ObjectFactory.class, org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.rs.ObjectFactory.class, org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.query.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Iti43PortType {

    /**
     * Retrieves a set of documents according to the ITI-43 specification.
     * @param body
     *          the request.
     * @return the response.
     */
    @WebResult(name = "RetrieveDocumentSetResponse", targetNamespace = "urn:ihe:iti:xds-b:2007", partName = "body")
    @Action(input = "urn:ihe:iti:2007:RetrieveDocumentSet", output = "urn:ihe:iti:2007:RetrieveDocumentSetResponse")
    @WebMethod(operationName = "DocumentRepository_RetrieveDocumentSet")
    public RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(
        @WebParam(partName = "body", name = "RetrieveDocumentSetRequest", targetNamespace = "urn:ihe:iti:xds-b:2007")
        RetrieveDocumentSetRequestType body
    );
}
