/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.iti62;

import org.apache.cxf.annotations.DataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.RemoveObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.Action;

/**
 * Provides the ITI-62 web-service interface.
 */
@WebService(targetNamespace = "urn:ihe:iti:xds-b:2010", name = "DocumentRegistry_PortType", portName = "DocumentRegistry_Port_Soap12")
@XmlSeeAlso({
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@DataBinding(XdsJaxbDataBinding.class)
public interface Iti62PortType {

    /**
     * Deletes a set of documents according to the ITI-62 specification.
     * @param body
     *          the request.
     * @return the response.
     */
    @WebResult(name = "RegistryResponse",
            targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0",
            partName = "body")
    @Action(input = "urn:ihe:iti:2010:DeleteDocumentSet", output = "urn:ihe:iti:2010:DeleteDocumentSetResponse")
    @WebMethod(operationName = "DocumentRegistry_DeleteDocumentSet")
    RegistryResponseType documentRegistryDeleteDocumentSet(
            @WebParam(partName = "body", name = "RemoveObjectsRequest", targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0")
                    RemoveObjectsRequest body
    );
}
