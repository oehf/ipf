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
package org.openehealth.ipf.commons.ihe.hpd.chpidd;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 * SEI for the CH-PIDD transaction (Swiss HPD extension)
 *
 * @author Dmytro Rud
 */
@WebService(targetNamespace = "urn:ihe:iti:hpd:2010", name = "ProviderInformationDirectory_PortType", portName = "ProviderInformationDirectory_Port_Soap12")
@XmlSeeAlso({org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.ObjectFactory.class, org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ChPiddPortType {

    @WebMethod(operationName = "ProviderInformationDownloadRequest")
    @Action(input = "urn:ihe:iti:hpd:2010:ProviderInformationDownloadRequest", output = "urn:ihe:iti:hpd:2010:ProviderInformationDownloadResponse")
    @WebResult(name = "downloadResponse", targetNamespace = "urn:ehealth-suisse:names:tc:CS:1", partName = "body")
    public org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadResponse providerInformationDownloadRequest(
            @WebParam(partName = "body", name = "downloadRequest", targetNamespace = "urn:ehealth-suisse:names:tc:CS:1")
                    org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadRequest body
    );
}
