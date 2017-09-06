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
package org.openehealth.ipf.commons.ihe.xds.itiX1;

import org.apache.cxf.annotations.DataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 * SEI for the the XCMU profile.
 * @since 3.4
 */
@WebService(targetNamespace = "urn:ihe:iti:xcmu:2017", name = "RespondingGateway_PortType", portName = "RespondingGateway_Port_Soap12")
@XmlSeeAlso({
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@DataBinding(XdsJaxbDataBinding.class)
public interface ItiX1PortType {

    @WebResult(name = "RegistryResponse",
            targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0",
            partName = "body")
    @Action(input = "urn:ihe:iti:2017:CrossGatewayUpdateDocumentSet", output = "urn:ihe:iti:2017:CrossGatewayUpdateDocumentSetResponse")
    @WebMethod(operationName = "RespondingGateway_CrossGatewayUpdateDocumentSet")
    public RegistryResponseType documentRegistryUpdateDocumentSet(
            @WebParam(partName = "body", name = "SubmitObjectsRequest", targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0")
                    SubmitObjectsRequest body
    );
}
