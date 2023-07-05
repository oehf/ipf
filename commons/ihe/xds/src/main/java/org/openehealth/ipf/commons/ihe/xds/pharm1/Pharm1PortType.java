/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.pharm1;

import org.apache.cxf.annotations.DataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.Action;

/**
 * Provides the PHARM-1 web-service interface.
 * @since 3.7
 */
@WebService(
    targetNamespace = "urn:ihe:iti:xds-b:2007",
    name = "CommunityPharmacyManager_PortType",
    portName = "CommunityPharmacyManager_Port_Soap12"
)
@XmlSeeAlso({
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class
})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@DataBinding(XdsJaxbDataBinding.class)
public interface Pharm1PortType {

    /**
     * Performs a stored query according to the PHARM-1 specification.
     * @param body
     *          the query request.
     * @return the query response.
     */
    @WebResult(
        name = "AdhocQueryResponse",
        targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0",
        partName = "body"
    )
    @Action(
        input = "urn:ihe:pharm:cmpd:2010:QueryPharmacyDocuments",
        output = "urn:ihe:pharm:cmpd:2010:QueryPharmacyDocumentsResponse"
    )
    @WebMethod(operationName = "CommunityPharmacyManager_QueryPharmacyDocuments")
    AdhocQueryResponse communityPharmacyManagerQueryPharmacyDocuments(
            @WebParam(
                    partName = "body",
                    name = "AdhocQueryRequest",
                    targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0")
                    AdhocQueryRequest body
    );
}
