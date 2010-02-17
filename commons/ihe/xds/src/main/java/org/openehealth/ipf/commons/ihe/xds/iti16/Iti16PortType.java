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
package org.openehealth.ipf.commons.ihe.xds.iti16;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryResponse;

/**
 * Provides the ITI-16 web-service interface.
 */
@WebService(targetNamespace = "urn:ihe:iti:xds:2007", name = "DocumentRegistry_PortType")
@XmlSeeAlso({org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.query.ObjectFactory.class, org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Iti16PortType {

    /**
     * Queries the registry according to the ITI-16 specification.
     * @param body
     *          the request.
     * @return the response.
     */
    @WebResult(name = "RegistryResponse", targetNamespace = "urn:oasis:names:tc:ebxml-regrep:registry:xsd:2.1", partName = "body")
    @Action(input = "urn:ihe:iti:2007:QueryRegistry", output = "urn:ihe:iti:2007:QueryRegistryResponse")
    @WebMethod(operationName = "DocumentRegistry_QueryRegistry")
    public RegistryResponse documentRegistryQueryRegistry(
        @WebParam(partName = "body", name = "AdhocQueryRequest", targetNamespace = "urn:oasis:names:tc:ebxml-regrep:query:xsd:2.1")
        AdhocQueryRequest body
    );
}
