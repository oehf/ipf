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
package org.openehealth.ipf.commons.ihe.xds.iti41;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.Action;

import org.apache.cxf.annotations.DataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

/**
 * Provides the ITI-41 web-service interface.
 */
@WebService(targetNamespace = "urn:ihe:iti:xds-b:2007", name = "DocumentRepository_PortType", portName = "DocumentRepository_Port_Soap12")
@XmlSeeAlso({
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@DataBinding(XdsJaxbDataBinding.class)
public interface Iti41PortType {

    /**
     * Provides and registers a set of documents according to the ITI-41 specification.
     * @param body
     *          the request.
     * @return the response.
     */
    @WebResult(name = "RegistryResponse", targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", partName = "body")
    @Action(input = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b", output = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse")
    @WebMethod(operationName = "DocumentRepository_ProvideAndRegisterDocumentSet-b")
    RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(
            @WebParam(partName = "body", name = "ProvideAndRegisterDocumentSetRequest", targetNamespace = "urn:ihe:iti:xds-b:2007")
                    ProvideAndRegisterDocumentSetRequestType body
    );
}
