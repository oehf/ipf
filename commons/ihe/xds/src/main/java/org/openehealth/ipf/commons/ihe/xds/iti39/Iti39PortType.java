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
package org.openehealth.ipf.commons.ihe.xds.iti39;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.Action;

import org.apache.cxf.annotations.DataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;

/**
 * ITI-39 SEI.
 */
@WebService(targetNamespace = "urn:ihe:iti:xds-b:2007", name = "RespondingGateway_PortType", portName = "RespondingGateway_Port_Soap12")
@XmlSeeAlso({
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@DataBinding(XdsJaxbDataBinding.class)
public interface Iti39PortType {

    /**
     * Retrieves a set of documents according to the ITI-39 specification.
     */
    @WebResult(name = "RetrieveDocumentSetResponse", targetNamespace = "urn:ihe:iti:xds-b:2007", partName = "body")
    @Action(input = "urn:ihe:iti:2007:CrossGatewayRetrieve", output = "urn:ihe:iti:2007:CrossGatewayRetrieveResponse")
    @WebMethod(operationName = "RespondingGateway_CrossGatewayRetrieve")
    RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(
            @WebParam(partName = "body", name = "RetrieveDocumentSetRequest", targetNamespace = "urn:ihe:iti:xds-b:2007")
                    RetrieveDocumentSetRequestType body
    );
}
