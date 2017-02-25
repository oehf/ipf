/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.iti58;

import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ObjectFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

@WebService(targetNamespace = "urn:ihe:iti:hpd:2010", name = "ProviderInformationDirectory_PortType", portName = "ProviderInformationDirectory_Port_Soap12")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Iti58PortType {

    @WebMethod(operationName = "ProviderInformationQueryRequest")
    @Action(input = "urn:ihe:iti:2010:ProviderInformationQuery", output = "urn:ihe:iti:2010:ProviderInformationQueryResponse")
    @WebResult(name = "batchResponse", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body")
    public BatchResponse providerInformationQueryRequest(
        @WebParam(partName = "body", name = "batchRequest", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core")
                BatchRequest body
    );
}
