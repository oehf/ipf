/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.chcidd;

import org.openehealth.ipf.commons.ihe.hpd.stub.chcidd.DownloadRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.chcidd.DownloadResponse;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.Action;

/**
 * SEI for the CH:CIDD transaction (Swiss EPR extension: Community Information Delta Download)
 *
 * @author Dmytro Rud
 */
@WebService(targetNamespace = "urn:ch:admin:bag:epr:cpi:2017", name = "ICommunityPortalIndex", portName = "WSHttpBinding_ICommunityPortalIndex")
@XmlSeeAlso({org.openehealth.ipf.commons.ihe.hpd.stub.chcidd.ObjectFactory.class, org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ChCiddPortType {

    @WebMethod(operationName = "CommunityDownloadRequest")
    @Action(input = "urn:ch:admin:bag:epr:2017:CommunityDownload", output = "urn:ch:admin:bag:epr:2017:CommunityDownloadResponse")
    @WebResult(name = "downloadResponse", targetNamespace = "urn:ch:admin:bag:epr:2017", partName = "body")
    DownloadResponse communityDownloadRequest(
            @WebParam(partName = "body", name = "downloadRequest", targetNamespace = "urn:ch:admin:bag:epr:2017")
                    DownloadRequest body
    );
}
