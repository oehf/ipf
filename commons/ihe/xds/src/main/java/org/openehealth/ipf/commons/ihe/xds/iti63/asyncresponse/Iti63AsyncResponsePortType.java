/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.iti63.asyncresponse;

import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 * ITI-63 Service Endpoint Interface for Asynchronous Response receiver
 */
@WebService(targetNamespace = "urn:ihe:iti:xds-b:2007", name = "InitiatingGateway_PortType")
@XmlSeeAlso({
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class
})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Iti63AsyncResponsePortType {

    @Oneway
    @Action(input = "urn:ihe:iti:2011:CrossGatewayFetchResponse")
    @WebMethod(operationName = "InitiatingGateway_Async_CrossGatewayFetch")
    public void crossGatewayFetchAsyncResponse(
            @WebParam(partName = "Body",
                    name = "AdhocQueryResponse",
                    targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0")
            AdhocQueryResponse body
    );
}
