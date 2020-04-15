/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.dispatch;

import org.apache.cxf.annotations.DataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

@WebService(targetNamespace = "urn:ihe:iti:xds-b:2007", name = "DocumentRegistry_PortType", portName = "DocumentRegistry_Port_Soap12")
@XmlSeeAlso({
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class
})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@DataBinding(XdsJaxbDataBinding.class)
public interface DocumentRegistryPortType {

    /**
     * Performs a stored query according to the ITI-18 specification.
     * @param body
     *          the query request.
     * @return the query response.
     */
    @WebResult(name = "AdhocQueryResponse",
            targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0",
            partName = "body")
    @Action(input = "urn:ihe:iti:2007:RegistryStoredQuery",
            output = "urn:ihe:iti:2007:RegistryStoredQueryResponse")
    @WebMethod(operationName = "DocumentRegistry_RegistryStoredQuery")
    AdhocQueryResponse documentRegistryRegistryStoredQuery(
            @WebParam(partName = "body",
                    name = "AdhocQueryRequest",
                    targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0")
                    AdhocQueryRequest body
    );

    /**
     * Registers a set of documents according to the ITI-42 specification.
     * @param body
     *          the request.
     * @return the response.
     */
    @WebResult(name = "RegistryResponse",
            targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0",
            partName = "body")
    @Action(input = "urn:ihe:iti:2007:RegisterDocumentSet-b",
            output = "urn:ihe:iti:2007:RegisterDocumentSet-bResponse")
    @WebMethod(operationName = "DocumentRegistry_RegisterDocumentSet-b")
    RegistryResponseType documentRegistryRegisterDocumentSetB(
            @WebParam(partName = "body",
                    name = "SubmitObjectsRequest",
                    targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0")
                    SubmitObjectsRequest body
    );

    /**
     * Performs a stored query according to the PHARM-1 specification.
     * @param body
     *          the query request.
     * @return the query response.
     */
    @WebResult(name = "AdhocQueryResponse",
            targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0",
            partName = "body")
    @Action(input = "urn:ihe:pharm:cmpd:2010:QueryPharmacyDocuments",
            output = "urn:ihe:pharm:cmpd:2010:QueryPharmacyDocumentsResponse")
    @WebMethod(operationName = "CommunityPharmacyManager_QueryPharmacyDocuments")
    AdhocQueryResponse communityPharmacyManagerQueryPharmacyDocuments(
            @WebParam(partName = "body",
                    name = "AdhocQueryRequest",
                    targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0")
                    AdhocQueryRequest body
    );
}
