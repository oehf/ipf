/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.iti92;

import org.apache.cxf.annotations.DataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.Action;

/**
 * SEI for the the RMU profile.
 * @since 3.4
 */
@WebService(targetNamespace = "urn:ihe:iti:rmu:2018", name = "UpdateResponder_PortType", portName = "UpdateResponder_Port_Soap12")
@XmlSeeAlso({
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@DataBinding(XdsJaxbDataBinding.class)
public interface Iti92PortType {

    @WebResult(name = "RegistryResponse",
            targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0",
            partName = "body")
    @Action(input = "urn:ihe:iti:2018:RestrictedUpdateDocumentSet", output = "urn:ihe:iti:2018:RestrictedUpdateDocumentSetResponse")
    @WebMethod(operationName = "UpdateResponder_RestrictedUpdateDocumentSet")
    RegistryResponseType restrictedUpdateDocumentSet(
            @WebParam(partName = "body", name = "SubmitObjectsRequest", targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0")
                    SubmitObjectsRequest body
    );
}
