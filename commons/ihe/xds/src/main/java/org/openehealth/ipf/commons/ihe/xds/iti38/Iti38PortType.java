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
package org.openehealth.ipf.commons.ihe.xds.iti38;

import org.apache.cxf.annotations.DataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 * ITI-38 Service Endpoint Interface.
 */
@WebService(targetNamespace = "urn:ihe:iti:xds-b:2007", name = "RespondingGateway_PortType")
@XmlSeeAlso({
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class
})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@DataBinding(XdsJaxbDataBinding.class)
public interface Iti38PortType {

    /**
     * Performs a stored query according to the ITI-38 specification.
     * @param body
     *          the query request.
     * @return the query response.
     */
    @WebResult(name = "AdhocQueryResponse",
            targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0",
            partName = "body")
    @Action(input = "urn:ihe:iti:2007:CrossGatewayQuery",
            output = "urn:ihe:iti:2007:CrossGatewayQueryResponse")
    @WebMethod(operationName = "RespondingGateway_CrossGatewayQuery")
    public AdhocQueryResponse documentRegistryRegistryStoredQuery(
        @WebParam(partName = "body",
                name = "AdhocQueryRequest",
                targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0")
        AdhocQueryRequest body
    );
}
