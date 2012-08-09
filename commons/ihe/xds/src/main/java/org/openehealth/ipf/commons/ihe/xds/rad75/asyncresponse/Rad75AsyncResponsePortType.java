/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.rad75.asyncresponse;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 * RAD-75 SEI for asynchronous response receiver.
 */
@WebService(targetNamespace = "urn:ihe:rad:xdsi-b:2009", name = "InitiatingGateway_PortType")
@XmlSeeAlso({
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Rad75AsyncResponsePortType
{

    @Oneway
    @Action(input = "urn:ihe:rad:2011:CrossGatewayRetrieveImagingDocumentSetResponse")
    @WebMethod(operationName = "InitiatingGateway_Async_CrossGatewayRetrieveImagingDocumentSet")
    public void documentRepositoryRetrieveImagingDocumentSet(
        @WebParam(partName = "body",
            name = "RetrieveDocumentSetResponse",
            targetNamespace = "urn:ihe:iti:xds-b:2007")
        RetrieveDocumentSetResponseType body
    );
}
