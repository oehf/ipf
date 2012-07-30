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

package org.openehealth.ipf.commons.ihe.xds.iti51;

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
 * Provides the ITI-51 web-service interface.
 */
@WebService(targetNamespace = "urn:ihe:iti:xds-b:2007", name = "DocumentRegistry_PortType")
@XmlSeeAlso({
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class
})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Iti51PortType {

    /**
     * Performs a stored query according to the ITI-51 specification.
     * @param body
     *          the query request.
     * @return the query response.
     */
    @WebResult(name = "AdhocQueryResponse",
            targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0",
            partName = "body")
    @Action(input = "urn:ihe:iti:2009:MultiPatientStoredQuery",
            output = "urn:ihe:iti:2009:MultiPatientStoredQueryResponse")
    @WebMethod(operationName = "DocumentRegistry_MultiPatientStoredQuery")
    public AdhocQueryResponse documentRegistryMultiPatientStoredQuery(
            @WebParam(partName = "body",
                    name = "AdhocQueryRequest",
                    targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0")
            AdhocQueryRequest body
    );
}
