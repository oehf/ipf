/*
 * Copyright 2025 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ech.ech0214;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2.Request;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2.Response;

/**
 * Port type for eCH-0214 request "Query SPID".
 */
@WebService(name = "SpidQueryServicePortTypeV2", targetNamespace = "http://www.zas.admin.ch/wupispid/ws/queryService/2", portName = "SpidQueryServicePortV2")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    org.openehealth.ipf.commons.ihe.ech.stub.ech_0006._2.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.ech.stub.ech_0007._5.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.ech.stub.ech_0008._3.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.ech.stub.ech_0010._5.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.ech.stub.ech_0058._5.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.ech.stub.ech_0135._1.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.ObjectFactory.class,
    org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2.ObjectFactory.class,
})
public interface Ech0214PortType {

    @WebMethod(operationName = "querySpid")
    @WebResult(name = "response", targetNamespace = "http://www.ech.ch/xmlns/eCH-0214/2", partName = "body")
    public Response querySpid(
        @WebParam(name = "request", targetNamespace = "http://www.ech.ch/xmlns/eCH-0214/2", partName = "body")
        Request body);

}
