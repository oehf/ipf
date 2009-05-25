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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti42.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 * Provides the ITI-42 web-service interface.
 */
@WebService(targetNamespace = "urn:ihe:iti:xds-b:2007", name = "DocumentRegistry_PortType")
@XmlSeeAlso({org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.rim.ObjectFactory.class, org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.lcm.ObjectFactory.class, org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.rs.ObjectFactory.class, org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.query.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Iti42PortType {

    /**
     * Registers a set of documents according to the ITI-42 specification.
     * @param body
     *          the request.
     * @return the response.
     */
    @WebResult(name = "RegistryResponse", targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", partName = "body")
    @Action(input = "urn:ihe:iti:2007:RegisterDocumentSet-b", output = "urn:ihe:iti:2007:RegisterDocumentSet-bResponse")
    @WebMethod(operationName = "DocumentRegistry_RegisterDocumentSet-b")
    public org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.rs.RegistryResponseType documentRegistryRegisterDocumentSetB(
        @WebParam(partName = "body", name = "SubmitObjectsRequest", targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0")
        org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.lcm.SubmitObjectsRequest body
    );
}
